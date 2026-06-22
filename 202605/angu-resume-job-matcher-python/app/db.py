import json
import os
import sqlite3
from pathlib import Path
from typing import Any, Iterable

from .common import camelize_row, now


def default_db_path() -> Path:
    root = Path(__file__).resolve().parents[2]
    rust_db = root / "angu-resume-job-matcher-rust" / "angu_resume_job_matcher.sqlite"
    if rust_db.exists():
        return rust_db
    return Path(__file__).resolve().parents[1] / "angu_resume_job_matcher.sqlite"


DB_PATH = Path(os.environ.get("DATABASE_PATH", default_db_path()))
UPLOAD_DIR = Path(os.environ.get("UPLOAD_DIR", Path(__file__).resolve().parents[1] / "uploads"))


def connect() -> sqlite3.Connection:
    UPLOAD_DIR.mkdir(parents=True, exist_ok=True)
    conn = sqlite3.connect(DB_PATH)
    conn.row_factory = sqlite3.Row
    init_db(conn)
    return conn


def rows(conn: sqlite3.Connection, sql: str, args: Iterable[Any] = ()) -> list[dict[str, Any]]:
    return [camelize_row(dict(r)) for r in conn.execute(sql, tuple(args)).fetchall()]


def row(conn: sqlite3.Connection, sql: str, args: Iterable[Any] = ()) -> dict[str, Any] | None:
    item = conn.execute(sql, tuple(args)).fetchone()
    return camelize_row(dict(item)) if item else None


def scalar(conn: sqlite3.Connection, sql: str, args: Iterable[Any] = ()) -> Any:
    item = conn.execute(sql, tuple(args)).fetchone()
    return item[0] if item else None


def init_db(conn: sqlite3.Connection) -> None:
    conn.executescript(
        """
        CREATE TABLE IF NOT EXISTS sys_user(id INTEGER PRIMARY KEY AUTOINCREMENT,username TEXT UNIQUE NOT NULL,real_name TEXT NOT NULL,phone TEXT UNIQUE NOT NULL,password TEXT NOT NULL,status INTEGER NOT NULL DEFAULT 1,error_count INTEGER NOT NULL DEFAULT 0,locked_until TEXT,deleted INTEGER NOT NULL DEFAULT 0,created_time TEXT NOT NULL,updated_time TEXT NOT NULL);
        CREATE TABLE IF NOT EXISTS sys_role(id INTEGER PRIMARY KEY AUTOINCREMENT,role_name TEXT NOT NULL,role_code TEXT UNIQUE NOT NULL,builtin INTEGER NOT NULL DEFAULT 0,status INTEGER NOT NULL DEFAULT 1,remark TEXT,created_time TEXT NOT NULL,updated_time TEXT NOT NULL);
        CREATE TABLE IF NOT EXISTS sys_menu(id INTEGER PRIMARY KEY,parent_id INTEGER NOT NULL DEFAULT 0,menu_type INTEGER NOT NULL,menu_name TEXT NOT NULL,path TEXT,perm_code TEXT,icon TEXT,sort INTEGER NOT NULL DEFAULT 0);
        CREATE TABLE IF NOT EXISTS sys_user_role(user_id INTEGER NOT NULL,role_id INTEGER NOT NULL,PRIMARY KEY(user_id,role_id));
        CREATE TABLE IF NOT EXISTS sys_role_menu(role_id INTEGER NOT NULL,menu_id INTEGER NOT NULL,PRIMARY KEY(role_id,menu_id));
        CREATE TABLE IF NOT EXISTS job_position(id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT NOT NULL,department TEXT,job_type TEXT,location TEXT,salary_min INTEGER,salary_max INTEGER,edu_require TEXT,exp_require INTEGER,description TEXT,skill_tags TEXT,status TEXT NOT NULL DEFAULT 'DRAFT',deleted INTEGER NOT NULL DEFAULT 0,creator_id INTEGER,created_time TEXT NOT NULL,updated_time TEXT NOT NULL);
        CREATE TABLE IF NOT EXISTS resume_main(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,phone TEXT,email TEXT,gender INTEGER,birth_date TEXT,city TEXT,desired_position TEXT,desired_city TEXT,desired_salary_min INTEGER,desired_salary_max INTEGER,job_status TEXT,highest_edu TEXT,total_exp_years INTEGER,file_path TEXT,parse_success INTEGER DEFAULT 0,source TEXT DEFAULT 'MANUAL',self_intro TEXT,deleted INTEGER NOT NULL DEFAULT 0,creator_id INTEGER,created_time TEXT NOT NULL,updated_time TEXT NOT NULL);
        CREATE TABLE IF NOT EXISTS resume_skill(id INTEGER PRIMARY KEY AUTOINCREMENT,resume_id INTEGER NOT NULL,skill_name TEXT NOT NULL);
        CREATE TABLE IF NOT EXISTS resume_education(id INTEGER PRIMARY KEY AUTOINCREMENT,resume_id INTEGER NOT NULL,school TEXT,major TEXT,edu_level TEXT,start_date TEXT,end_date TEXT);
        CREATE TABLE IF NOT EXISTS resume_work_exp(id INTEGER PRIMARY KEY AUTOINCREMENT,resume_id INTEGER NOT NULL,company TEXT,position TEXT,industry TEXT,start_date TEXT,end_date TEXT,description TEXT);
        CREATE TABLE IF NOT EXISTS match_config(id INTEGER PRIMARY KEY,skill_weight INTEGER NOT NULL,edu_weight INTEGER NOT NULL,exp_weight INTEGER NOT NULL,updated_time TEXT NOT NULL,updater_id INTEGER);
        CREATE TABLE IF NOT EXISTS match_result(id INTEGER PRIMARY KEY AUTOINCREMENT,position_id INTEGER NOT NULL,resume_id INTEGER NOT NULL,total_score REAL NOT NULL,skill_score REAL NOT NULL,edu_score REAL NOT NULL,exp_score REAL NOT NULL,matched_at TEXT NOT NULL);
        CREATE TABLE IF NOT EXISTS job_application(id INTEGER PRIMARY KEY AUTOINCREMENT,position_id INTEGER NOT NULL,resume_id INTEGER NOT NULL,status TEXT NOT NULL,operate_time TEXT NOT NULL,operator_id INTEGER,remark TEXT,created_time TEXT NOT NULL,UNIQUE(position_id,resume_id));
        CREATE TABLE IF NOT EXISTS application_log(id INTEGER PRIMARY KEY AUTOINCREMENT,application_id INTEGER NOT NULL,from_status TEXT,to_status TEXT NOT NULL,operator_id INTEGER,operator_name TEXT,remark TEXT,created_time TEXT NOT NULL);
        CREATE TABLE IF NOT EXISTS interview_record(id INTEGER PRIMARY KEY AUTOINCREMENT,application_id INTEGER NOT NULL,interview_time TEXT,interviewer TEXT,location TEXT,score INTEGER,comment TEXT,result TEXT,created_time TEXT NOT NULL);
        """
    )
    if scalar(conn, "SELECT COUNT(*) FROM sys_user"):
        return
    seed(conn)


def seed(conn: sqlite3.Connection) -> None:
    t = "2026-05-27 14:37:46"
    conn.execute("INSERT INTO sys_user(id,username,real_name,phone,password,status,error_count,deleted,created_time,updated_time) VALUES(1,'admin','System Admin','13800000000','$2a$10$CHHVPySZmO5DsDtGjAXwXugfDw4hPiI4HiOGoc8MSuyJqVM1e000q',1,0,0,?,?)", (t, t))
    conn.executemany("INSERT INTO sys_role(id,role_name,role_code,builtin,status,remark,created_time,updated_time) VALUES(?,?,?,?,?,?,?,?)", [
        (1, "Super Admin", "SUPER_ADMIN", 1, 1, "Built-in super admin", t, t),
        (2, "HR Admin", "HR_ADMIN", 0, 1, "HR admin", t, t),
        (3, "HR Staff", "HR_STAFF", 0, 1, "HR staff", t, t),
    ])
    conn.execute("INSERT INTO sys_user_role(user_id,role_id) VALUES(1,1)")
    menus = [
        (1,0,0,"System","/system",None,"setting",1),(2,0,0,"Jobs","/jobs",None,"briefcase",2),(3,0,0,"Resumes","/resumes",None,"file",3),(4,0,0,"Match","/match",None,"search",4),(5,0,0,"Recruitment Flow","/flow",None,"flow",5),(6,0,0,"Reports","/stats",None,"bar-chart",6),
        (11,1,1,"Users","/system/users",None,"user",1),(12,1,1,"Roles","/system/roles",None,"team",2),(13,1,1,"Menus","/system/menus",None,"menu",3),(21,2,1,"Job Management","/jobs/list",None,"briefcase",1),(31,3,1,"Resume Management","/resumes/list",None,"file",1),(41,4,1,"Match Management","/match/list",None,"search",1),(51,5,1,"Applications","/applications",None,"ordered-list",1),(52,5,1,"Interviews","/interviews",None,"calendar",2),(61,6,1,"Dashboard","/stats/dashboard",None,"dashboard",1),(62,6,1,"Source Report","/stats/source",None,"pie-chart",2),
    ]
    perms = [(1101,11,"User List","system:user:list",1),(1102,11,"User Add","system:user:add",2),(1103,11,"User Edit","system:user:edit",3),(1104,11,"User Delete","system:user:delete",4),(1201,12,"Role List","system:role:list",1),(1202,12,"Role Add","system:role:add",2),(1203,12,"Role Edit","system:role:edit",3),(1204,12,"Role Delete","system:role:delete",4),(1301,13,"Menu List","system:menu:list",1),(1302,13,"Menu Add","system:menu:add",2),(1303,13,"Menu Edit","system:menu:edit",3),(1304,13,"Menu Delete","system:menu:delete",4),(2101,21,"Job List","job:job:list",1),(2102,21,"Job Add","job:job:add",2),(2103,21,"Job Edit","job:job:edit",3),(2104,21,"Job Delete","job:job:delete",4),(2105,21,"Job Publish","job:job:publish",5),(2106,21,"Job Close","job:job:close",6),(3101,31,"Resume List","resume:resume:list",1),(3102,31,"Resume Add","resume:resume:add",2),(3103,31,"Resume Edit","resume:resume:edit",3),(3104,31,"Resume Delete","resume:resume:delete",4),(3105,31,"Resume Export","resume:resume:export",5),(4101,41,"Match Execute","match:match:execute",1),(4102,41,"Match Config View","match:config:view",2),(4103,41,"Match Config Edit","match:config:edit",3),(5101,51,"Application List","application:list",1),(5102,51,"Application Add","application:add",2),(5103,51,"Application Edit","application:edit",3),(5201,52,"Interview List","interview:list",1),(5202,52,"Interview Add","interview:add",2),(5203,52,"Interview Edit","interview:edit",3),(6101,61,"Dashboard View","stats:dashboard:view",1),(6201,62,"Source Report View","stats:report:view",1),(6202,62,"Source Report Export","stats:report:export",2)]
    conn.executemany("INSERT INTO sys_menu(id,parent_id,menu_type,menu_name,path,perm_code,icon,sort) VALUES(?,?,?,?,?,?,?,?)", menus)
    conn.executemany("INSERT INTO sys_menu(id,parent_id,menu_type,menu_name,path,perm_code,icon,sort) VALUES(?,?,2,?,NULL,?,NULL,?)", perms)
    conn.executemany("INSERT INTO sys_role_menu(role_id,menu_id) VALUES(1,?)", [(x["id"],) for x in rows(conn, "SELECT id FROM sys_menu")])
    conn.execute("INSERT INTO job_position(id,title,department,job_type,location,salary_min,salary_max,edu_require,exp_require,description,skill_tags,status,deleted,creator_id,created_time,updated_time) VALUES(1,'自动化测试职位-0527','智能招聘部','FULL_TIME','杭州',10000,20000,'BACHELOR',1,'自动化测试创建的职位，用于验证前端主流程。',?,'OPEN',0,1,'2026-05-27 16:17:13','2026-05-27 16:18:52')", (json.dumps(["Vue3","TypeScript","招聘系统"], ensure_ascii=False),))
    conn.execute("INSERT INTO resume_main(id,name,phone,email,city,desired_position,desired_city,job_status,source,self_intro,deleted,creator_id,created_time,updated_time) VALUES(1,'自动化测试候选人0527','13900005270','auto0527@example.com','杭州','前端开发工程师','杭州','','MANUAL','自动化测试创建的简历，用于验证匹配、投递和面试流程。',0,1,'2026-05-27 16:18:40','2026-05-27 16:18:40'),(2,'自动化回归候选人0527B','13900005271','auto0527b@example.com','杭州','前端开发工程师','杭州',NULL,'MANUAL','用于验证面试创建自动推进状态',0,1,'2026-05-27 16:36:50','2026-05-27 16:36:50')")
    conn.execute("INSERT INTO resume_skill(id,resume_id,skill_name) VALUES(1,1,'Vue3'),(2,1,'TypeScript'),(3,1,'招聘流程'),(4,2,'Vue3'),(5,2,'TypeScript')")
    conn.execute("INSERT INTO match_config(id,skill_weight,edu_weight,exp_weight,updated_time,updater_id) VALUES(1,50,30,20,?,1)", (now(),))
    conn.execute("INSERT INTO job_application(id,position_id,resume_id,status,operate_time,operator_id,remark,created_time) VALUES(1,1,1,'INTERVIEW_PASSED','2026-05-27 16:29:53',1,'面试结果：PASS','2026-05-27 16:19:47'),(2,1,2,'INTERVIEW_PASSED','2026-05-27 16:37:31',1,'面试结果：PASS','2026-05-27 16:36:50')")
    conn.execute("INSERT INTO interview_record(id,application_id,interview_time,interviewer,location,score,comment,result,created_time) VALUES(1,1,'2026-05-27 19:30:00','面试官A','线上会议室-自动化',5,'接口测试面试通过','PASS','2026-05-27 16:29:44'),(2,2,'2026-05-27 20:00:00','面试官B','线上会议室-回归',5,'回归面试通过','PASS','2026-05-27 16:36:50')")
    conn.commit()
