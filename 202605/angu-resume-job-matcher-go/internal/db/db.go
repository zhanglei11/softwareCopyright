package db

import (
	"database/sql"
	"encoding/json"
	"os"
	"path/filepath"
	"strings"

	_ "modernc.org/sqlite"
)

func DefaultPath() string {
	root := "/Users/zhanglei/cz/project/softwareCopyright/202605"
	rustDB := filepath.Join(root, "angu-resume-job-matcher-rust", "angu_resume_job_matcher.sqlite")
	if _, err := os.Stat(rustDB); err == nil {
		return rustDB
	}
	return filepath.Join(root, "angu-resume-job-matcher-go", "angu_resume_job_matcher.sqlite")
}

func Open() (*sql.DB, error) {
	path := os.Getenv("DATABASE_PATH")
	if path == "" {
		path = DefaultPath()
	}
	database, err := sql.Open("sqlite", path)
	if err != nil {
		return nil, err
	}
	if err := Init(database); err != nil {
		return nil, err
	}
	return database, nil
}

func Init(database *sql.DB) error {
	schema := `
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
CREATE TABLE IF NOT EXISTS interview_record(id INTEGER PRIMARY KEY AUTOINCREMENT,application_id INTEGER NOT NULL,interview_time TEXT,interviewer TEXT,location TEXT,score INTEGER,comment TEXT,result TEXT,created_time TEXT NOT NULL);`
	if _, err := database.Exec(schema); err != nil {
		return err
	}
	var count int
	_ = database.QueryRow("SELECT COUNT(*) FROM sys_user").Scan(&count)
	if count > 0 {
		return nil
	}
	return seed(database)
}

func seed(database *sql.DB) error {
	t := "2026-05-27 14:37:46"
	stmts := []string{
		"INSERT INTO sys_user(id,username,real_name,phone,password,status,error_count,deleted,created_time,updated_time) VALUES(1,'admin','System Admin','13800000000','$2a$10$CHHVPySZmO5DsDtGjAXwXugfDw4hPiI4HiOGoc8MSuyJqVM1e000q',1,0,0,'" + t + "','" + t + "')",
		"INSERT INTO sys_role(id,role_name,role_code,builtin,status,remark,created_time,updated_time) VALUES(1,'Super Admin','SUPER_ADMIN',1,1,'Built-in super admin','" + t + "','" + t + "'),(2,'HR Admin','HR_ADMIN',0,1,'HR admin','" + t + "','" + t + "'),(3,'HR Staff','HR_STAFF',0,1,'HR staff','" + t + "','" + t + "')",
		"INSERT INTO sys_user_role(user_id,role_id) VALUES(1,1)",
	}
	for _, stmt := range stmts {
		if _, err := database.Exec(stmt); err != nil {
			return err
		}
	}
	menus := []struct{ id, pid, typ, sort int; name, path, icon string }{
		{1, 0, 0, 1, "System", "/system", "setting"}, {2, 0, 0, 2, "Jobs", "/jobs", "briefcase"}, {3, 0, 0, 3, "Resumes", "/resumes", "file"}, {4, 0, 0, 4, "Match", "/match", "search"}, {5, 0, 0, 5, "Recruitment Flow", "/flow", "flow"}, {6, 0, 0, 6, "Reports", "/stats", "bar-chart"},
		{11, 1, 1, 1, "Users", "/system/users", "user"}, {12, 1, 1, 2, "Roles", "/system/roles", "team"}, {13, 1, 1, 3, "Menus", "/system/menus", "menu"}, {21, 2, 1, 1, "Job Management", "/jobs/list", "briefcase"}, {31, 3, 1, 1, "Resume Management", "/resumes/list", "file"}, {41, 4, 1, 1, "Match Management", "/match/list", "search"}, {51, 5, 1, 1, "Applications", "/applications", "ordered-list"}, {52, 5, 1, 2, "Interviews", "/interviews", "calendar"}, {61, 6, 1, 1, "Dashboard", "/stats/dashboard", "dashboard"}, {62, 6, 1, 2, "Source Report", "/stats/source", "pie-chart"},
	}
	for _, m := range menus {
		_, _ = database.Exec("INSERT INTO sys_menu(id,parent_id,menu_type,menu_name,path,icon,sort) VALUES(?,?,?,?,?,?,?)", m.id, m.pid, m.typ, m.name, m.path, m.icon, m.sort)
	}
	perms := []struct{ id, pid, sort int; name, perm string }{
		{1101, 11, 1, "User List", "system:user:list"}, {1102, 11, 2, "User Add", "system:user:add"}, {1103, 11, 3, "User Edit", "system:user:edit"}, {1104, 11, 4, "User Delete", "system:user:delete"}, {2101, 21, 1, "Job List", "job:job:list"}, {2102, 21, 2, "Job Add", "job:job:add"}, {3101, 31, 1, "Resume List", "resume:resume:list"}, {4101, 41, 1, "Match Execute", "match:match:execute"}, {5101, 51, 1, "Application List", "application:list"}, {5201, 52, 1, "Interview List", "interview:list"}, {6101, 61, 1, "Dashboard View", "stats:dashboard:view"},
	}
	for _, p := range perms {
		_, _ = database.Exec("INSERT INTO sys_menu(id,parent_id,menu_type,menu_name,perm_code,sort) VALUES(?,?,2,?,?,?)", p.id, p.pid, p.name, p.perm, p.sort)
	}
	rows, _ := database.Query("SELECT id FROM sys_menu")
	defer rows.Close()
	for rows.Next() {
		var id int
		_ = rows.Scan(&id)
		_, _ = database.Exec("INSERT INTO sys_role_menu(role_id,menu_id) VALUES(1,?)", id)
	}
	skills, _ := json.Marshal([]string{"Vue3", "TypeScript", "招聘系统"})
	_, _ = database.Exec("INSERT INTO job_position(id,title,department,job_type,location,salary_min,salary_max,edu_require,exp_require,description,skill_tags,status,deleted,creator_id,created_time,updated_time) VALUES(1,'自动化测试职位-0527','智能招聘部','FULL_TIME','杭州',10000,20000,'BACHELOR',1,'自动化测试创建的职位，用于验证前端主流程。',?,'OPEN',0,1,'2026-05-27 16:17:13','2026-05-27 16:18:52')", string(skills))
	_, _ = database.Exec("INSERT INTO resume_main(id,name,phone,email,city,desired_position,desired_city,job_status,source,self_intro,deleted,creator_id,created_time,updated_time) VALUES(1,'自动化测试候选人0527','13900005270','auto0527@example.com','杭州','前端开发工程师','杭州','','MANUAL','自动化测试创建的简历，用于验证匹配、投递和面试流程。',0,1,'2026-05-27 16:18:40','2026-05-27 16:18:40'),(2,'自动化回归候选人0527B','13900005271','auto0527b@example.com','杭州','前端开发工程师','杭州',NULL,'MANUAL','用于验证面试创建自动推进状态',0,1,'2026-05-27 16:36:50','2026-05-27 16:36:50')")
	_, _ = database.Exec("INSERT INTO resume_skill(id,resume_id,skill_name) VALUES(1,1,'Vue3'),(2,1,'TypeScript'),(3,1,'招聘流程'),(4,2,'Vue3'),(5,2,'TypeScript')")
	_, _ = database.Exec("INSERT INTO match_config(id,skill_weight,edu_weight,exp_weight,updated_time,updater_id) VALUES(1,50,30,20,'2026-05-27 16:59:08',1)")
	_, _ = database.Exec("INSERT INTO job_application(id,position_id,resume_id,status,operate_time,operator_id,remark,created_time) VALUES(1,1,1,'INTERVIEW_PASSED','2026-05-27 16:29:53',1,'面试结果：PASS','2026-05-27 16:19:47'),(2,1,2,'INTERVIEW_PASSED','2026-05-27 16:37:31',1,'面试结果：PASS','2026-05-27 16:36:50')")
	_, _ = database.Exec("INSERT INTO interview_record(id,application_id,interview_time,interviewer,location,score,comment,result,created_time) VALUES(1,1,'2026-05-27 19:30:00','面试官A','线上会议室-自动化',5,'接口测试面试通过','PASS','2026-05-27 16:29:44'),(2,2,'2026-05-27 20:00:00','面试官B','线上会议室-回归',5,'回归面试通过','PASS','2026-05-27 16:36:50')")
	return nil
}

func Query(database *sql.DB, sqlText string, args ...interface{}) ([]map[string]interface{}, error) {
	rs, err := database.Query(sqlText, args...)
	if err != nil {
		return nil, err
	}
	defer rs.Close()
	cols, _ := rs.Columns()
	out := []map[string]interface{}{}
	for rs.Next() {
		values := make([]interface{}, len(cols))
		ptrs := make([]interface{}, len(cols))
		for i := range values {
			ptrs[i] = &values[i]
		}
		_ = rs.Scan(ptrs...)
		item := map[string]interface{}{}
		for i, col := range cols {
			item[Camel(col)] = normalize(values[i])
		}
		out = append(out, item)
	}
	return out, nil
}

func One(database *sql.DB, sqlText string, args ...interface{}) map[string]interface{} {
	items, _ := Query(database, sqlText, args...)
	if len(items) == 0 {
		return nil
	}
	return items[0]
}

func ScalarInt(database *sql.DB, sqlText string, args ...interface{}) int64 {
	var n int64
	_ = database.QueryRow(sqlText, args...).Scan(&n)
	return n
}

func normalize(v interface{}) interface{} {
	switch x := v.(type) {
	case []byte:
		return string(x)
	default:
		return x
	}
}

func Camel(s string) string {
	mapping := map[string]string{
		"real_name": "realName", "error_count": "errorCount", "locked_until": "lockedUntil", "created_time": "createdTime", "updated_time": "updatedTime", "parent_id": "parentId", "menu_type": "menuType", "menu_name": "menuName", "perm_code": "permCode", "role_name": "roleName", "role_code": "roleCode", "job_type": "jobType", "salary_min": "salaryMin", "salary_max": "salaryMax", "edu_require": "eduRequire", "exp_require": "expRequire", "skill_tags": "skillTags", "creator_id": "creatorId", "birth_date": "birthDate", "desired_position": "desiredPosition", "desired_city": "desiredCity", "desired_salary_min": "desiredSalaryMin", "desired_salary_max": "desiredSalaryMax", "job_status": "jobStatus", "highest_edu": "highestEdu", "total_exp_years": "totalExpYears", "file_path": "filePath", "parse_success": "parseSuccess", "resume_id": "resumeId", "skill_name": "skillName", "edu_level": "eduLevel", "start_date": "startDate", "end_date": "endDate", "position_id": "positionId", "total_score": "totalScore", "skill_score": "skillScore", "edu_score": "eduScore", "exp_score": "expScore", "matched_at": "matchedAt", "operate_time": "operateTime", "operator_id": "operatorId", "position_title": "positionTitle", "resume_name": "resumeName", "resume_phone": "resumePhone", "application_id": "applicationId", "from_status": "fromStatus", "to_status": "toStatus", "operator_name": "operatorName", "interview_time": "interviewTime", "skill_weight": "skillWeight", "edu_weight": "eduWeight", "exp_weight": "expWeight", "updater_id": "updaterId",
	}
	if v, ok := mapping[s]; ok {
		return v
	}
	parts := strings.Split(s, "_")
	for i := 1; i < len(parts); i++ {
		if len(parts[i]) > 0 {
			parts[i] = strings.ToUpper(parts[i][:1]) + parts[i][1:]
		}
	}
	return strings.Join(parts, "")
}
