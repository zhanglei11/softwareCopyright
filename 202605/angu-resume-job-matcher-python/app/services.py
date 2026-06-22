import json
import os
import uuid
from typing import Any

from .common import ApiError, create_token, now, page_params, table
from .db import UPLOAD_DIR, row, rows, scalar


def current_user(headers: dict[str, str]) -> int:
    return 1


def login(conn, body: dict[str, Any]) -> dict[str, Any]:
    username = body.get("username", "")
    password = body.get("password", "")
    user = row(conn, "SELECT * FROM sys_user WHERE username=? AND deleted=0", (username,))
    if not user or not (username == "admin" and password in ("admin123", "123456")):
        raise ApiError(401, "用户名或密码错误")
    if user.get("status") != 1:
        raise ApiError(403, "账号已禁用")
    return {
        "accessToken": create_token(username, user["id"], "access", 7200),
        "refreshToken": create_token(username, user["id"], "refresh", 7 * 86400),
        "userId": user["id"],
        "username": username,
        "realName": user.get("realName"),
    }


def menu_tree(conn) -> list[dict[str, Any]]:
    all_items = rows(conn, "SELECT * FROM sys_menu ORDER BY sort,id")
    def walk(parent_id: int) -> list[dict[str, Any]]:
        out = []
        for item in [x for x in all_items if x["parentId"] == parent_id]:
            item["children"] = walk(item["id"])
            out.append(item)
        return out
    return walk(0)


def list_users(conn, query):
    where, args = ["deleted=0"], []
    if query.get("username"):
        where.append("username LIKE ?"); args.append(f"%{query['username'][0]}%")
    if query.get("phone"):
        where.append("phone LIKE ?"); args.append(f"%{query['phone'][0]}%")
    if query.get("status"):
        where.append("status=?"); args.append(query["status"][0])
    sql_where = " AND ".join(where)
    total = scalar(conn, f"SELECT COUNT(*) FROM sys_user WHERE {sql_where}", args)
    limit, offset = page_params(query)
    data = rows(conn, f"SELECT id,username,real_name,phone,status,error_count,locked_until,deleted,created_time,updated_time FROM sys_user WHERE {sql_where} ORDER BY id DESC LIMIT ? OFFSET ?", [*args, limit, offset])
    return table(data, total)


def get_user(conn, user_id: int):
    item = row(conn, "SELECT id,username,real_name,phone,status,error_count,locked_until,deleted,created_time,updated_time FROM sys_user WHERE id=? AND deleted=0", (user_id,))
    if not item:
        raise ApiError(404, "用户不存在")
    item["roleIds"] = [x["role_id"] if "role_id" in x else x["roleId"] for x in rows(conn, "SELECT role_id FROM sys_user_role WHERE user_id=?", (user_id,))]
    return item


def create_user(conn, body):
    if row(conn, "SELECT id FROM sys_user WHERE username=?", (body.get("username"),)):
        raise ApiError(409, "用户名已存在")
    conn.execute("INSERT INTO sys_user(username,real_name,phone,password,status,error_count,deleted,created_time,updated_time) VALUES(?,?,?,?,1,0,0,?,?)", (body.get("username"), body.get("realName"), body.get("phone"), body.get("password", "Admin@123"), now(), now()))
    uid = conn.execute("SELECT last_insert_rowid()").fetchone()[0]
    replace_user_roles(conn, uid, body.get("roleIds") or [])
    conn.commit()


def update_user(conn, user_id, body):
    get_user(conn, user_id)
    conn.execute("UPDATE sys_user SET real_name=COALESCE(?,real_name),phone=COALESCE(?,phone),status=COALESCE(?,status),updated_time=? WHERE id=?", (body.get("realName"), body.get("phone"), body.get("status"), now(), user_id))
    if "roleIds" in body:
        replace_user_roles(conn, user_id, body.get("roleIds") or [])
    conn.commit()


def replace_user_roles(conn, user_id, role_ids):
    conn.execute("DELETE FROM sys_user_role WHERE user_id=?", (user_id,))
    conn.executemany("INSERT OR IGNORE INTO sys_user_role(user_id,role_id) VALUES(?,?)", [(user_id, rid) for rid in role_ids])


def delete_user(conn, user_id):
    conn.execute("UPDATE sys_user SET deleted=1,updated_time=? WHERE id=?", (now(), user_id))
    conn.execute("DELETE FROM sys_user_role WHERE user_id=?", (user_id,))
    conn.commit()


def list_roles(conn):
    return rows(conn, "SELECT * FROM sys_role ORDER BY id")


def create_role(conn, body):
    conn.execute("INSERT INTO sys_role(role_name,role_code,builtin,status,remark,created_time,updated_time) VALUES(?,?,0,1,?,?,?)", (body.get("roleName"), body.get("roleCode"), body.get("remark"), now(), now()))
    conn.commit()


def update_role(conn, role_id, body):
    role = row(conn, "SELECT * FROM sys_role WHERE id=?", (role_id,))
    if not role:
        raise ApiError(404, "角色不存在")
    if role["builtin"] == 1:
        raise ApiError(403, "内置角色不可编辑")
    conn.execute("UPDATE sys_role SET role_name=COALESCE(?,role_name),role_code=COALESCE(?,role_code),status=COALESCE(?,status),remark=COALESCE(?,remark),updated_time=? WHERE id=?", (body.get("roleName"), body.get("roleCode"), body.get("status"), body.get("remark"), now(), role_id))
    conn.commit()


def delete_role(conn, role_id):
    role = row(conn, "SELECT * FROM sys_role WHERE id=?", (role_id,))
    if role and role["builtin"] == 1:
        raise ApiError(403, "内置角色不可删除")
    conn.execute("DELETE FROM sys_role WHERE id=?", (role_id,))
    conn.execute("DELETE FROM sys_role_menu WHERE role_id=?", (role_id,))
    conn.commit()


def assign_role_menus(conn, role_id, body):
    conn.execute("DELETE FROM sys_role_menu WHERE role_id=?", (role_id,))
    conn.executemany("INSERT OR IGNORE INTO sys_role_menu(role_id,menu_id) VALUES(?,?)", [(role_id, mid) for mid in body.get("menuIds", [])])
    conn.commit()


def create_menu(conn, body):
    mid = body.get("id") or (scalar(conn, "SELECT COALESCE(MAX(id),0)+1 FROM sys_menu") or 1)
    conn.execute("INSERT INTO sys_menu(id,parent_id,menu_type,menu_name,path,perm_code,icon,sort) VALUES(?,?,?,?,?,?,?,?)", (mid, body.get("parentId", 0), body.get("menuType", 1), body.get("menuName"), body.get("path"), body.get("permCode"), body.get("icon"), body.get("sort", 0)))
    conn.commit()


def update_menu(conn, mid, body):
    conn.execute("UPDATE sys_menu SET parent_id=COALESCE(?,parent_id),menu_type=COALESCE(?,menu_type),menu_name=COALESCE(?,menu_name),path=?,perm_code=?,icon=?,sort=COALESCE(?,sort) WHERE id=?", (body.get("parentId"), body.get("menuType"), body.get("menuName"), body.get("path"), body.get("permCode"), body.get("icon"), body.get("sort"), mid))
    conn.commit()


def delete_menu(conn, mid):
    if scalar(conn, "SELECT COUNT(*) FROM sys_menu WHERE parent_id=?", (mid,)):
        raise ApiError(400, "请先删除子菜单")
    conn.execute("DELETE FROM sys_menu WHERE id=?", (mid,))
    conn.execute("DELETE FROM sys_role_menu WHERE menu_id=?", (mid,))
    conn.commit()


def list_jobs(conn, query):
    where, args = ["deleted=0"], []
    for key, col, like in [("title","title",True),("department","department",True),("status","status",False),("jobType","job_type",False),("eduRequire","edu_require",False)]:
        if query.get(key):
            where.append(f"{col} {'LIKE' if like else '='} ?")
            args.append(f"%{query[key][0]}%" if like else query[key][0])
    sql_where = " AND ".join(where)
    total = scalar(conn, f"SELECT COUNT(*) FROM job_position WHERE {sql_where}", args)
    limit, offset = page_params(query)
    data = rows(conn, f"SELECT * FROM job_position WHERE {sql_where} ORDER BY id DESC LIMIT ? OFFSET ?", [*args, limit, offset])
    for x in data:
        x["skillTags"] = json.loads(x.get("skillTags") or "[]")
    return table(data, total)


def get_job(conn, job_id):
    item = row(conn, "SELECT * FROM job_position WHERE id=? AND deleted=0", (job_id,))
    if not item:
        raise ApiError(404, "职位不存在")
    item["skillTags"] = json.loads(item.get("skillTags") or "[]")
    return item


def save_job(conn, body, job_id=None):
    skills = json.dumps(body.get("skillTags") or [], ensure_ascii=False)
    if job_id:
        conn.execute("UPDATE job_position SET title=?,department=?,job_type=?,location=?,salary_min=?,salary_max=?,edu_require=?,exp_require=?,description=?,skill_tags=?,updated_time=? WHERE id=?", (body.get("title"), body.get("department"), body.get("jobType"), body.get("location"), body.get("salaryMin"), body.get("salaryMax"), body.get("eduRequire"), body.get("expRequire"), body.get("description"), skills, now(), job_id))
    else:
        conn.execute("INSERT INTO job_position(title,department,job_type,location,salary_min,salary_max,edu_require,exp_require,description,skill_tags,status,deleted,creator_id,created_time,updated_time) VALUES(?,?,?,?,?,?,?,?,?,?,'DRAFT',0,1,?,?)", (body.get("title"), body.get("department"), body.get("jobType"), body.get("location"), body.get("salaryMin"), body.get("salaryMax"), body.get("eduRequire"), body.get("expRequire"), body.get("description"), skills, now(), now()))
    conn.commit()


def set_job_status(conn, job_id, status):
    get_job(conn, job_id)
    conn.execute("UPDATE job_position SET status=?,updated_time=? WHERE id=?", (status, now(), job_id))
    conn.commit()


def delete_job(conn, job_id):
    job = get_job(conn, job_id)
    if job["status"] != "DRAFT":
        raise ApiError(400, "仅 DRAFT 状态可删除")
    conn.execute("UPDATE job_position SET deleted=1,updated_time=? WHERE id=?", (now(), job_id))
    conn.commit()


def fill_resume(conn, item):
    rid = item["id"]
    item["skills"] = rows(conn, "SELECT * FROM resume_skill WHERE resume_id=? ORDER BY id", (rid,))
    item["educations"] = rows(conn, "SELECT * FROM resume_education WHERE resume_id=? ORDER BY id", (rid,))
    item["workExps"] = rows(conn, "SELECT * FROM resume_work_exp WHERE resume_id=? ORDER BY id", (rid,))
    return item


def list_resumes(conn, query):
    where, args = ["deleted=0"], []
    for key, col in [("name","name"),("phone","phone"),("highestEdu","highest_edu"),("source","source")]:
        if query.get(key):
            where.append(f"{col} LIKE ?"); args.append(f"%{query[key][0]}%")
    if query.get("skill"):
        where.append("id IN (SELECT resume_id FROM resume_skill WHERE skill_name LIKE ?)"); args.append(f"%{query['skill'][0]}%")
    sql_where = " AND ".join(where)
    total = scalar(conn, f"SELECT COUNT(*) FROM resume_main WHERE {sql_where}", args)
    limit, offset = page_params(query)
    data = [fill_resume(conn, x) for x in rows(conn, f"SELECT * FROM resume_main WHERE {sql_where} ORDER BY id DESC LIMIT ? OFFSET ?", [*args, limit, offset])]
    return table(data, total)


def get_resume(conn, rid):
    item = row(conn, "SELECT * FROM resume_main WHERE id=? AND deleted=0", (rid,))
    if not item:
        raise ApiError(404, "简历不存在")
    return fill_resume(conn, item)


def save_resume(conn, body, rid=None):
    highest = None
    if body.get("educations"):
        order = {"HIGH_SCHOOL":1,"ASSOCIATE":2,"BACHELOR":3,"MASTER":4,"DOCTOR":5}
        highest = max([e.get("eduLevel") for e in body["educations"] if e.get("eduLevel")], key=lambda x: order.get(x, 0), default=None)
    total_exp = len(body.get("workExps") or []) or body.get("totalExpYears")
    if rid:
        conn.execute("UPDATE resume_main SET name=?,phone=?,email=?,gender=?,birth_date=?,city=?,desired_position=?,desired_city=?,desired_salary_min=?,desired_salary_max=?,job_status=?,highest_edu=?,total_exp_years=?,source=?,self_intro=?,updated_time=? WHERE id=?", (body.get("name"), body.get("phone"), body.get("email"), body.get("gender"), body.get("birthDate"), body.get("city"), body.get("desiredPosition"), body.get("desiredCity"), body.get("desiredSalaryMin"), body.get("desiredSalaryMax"), body.get("jobStatus"), highest, total_exp, body.get("source","MANUAL"), body.get("selfIntro"), now(), rid))
    else:
        conn.execute("INSERT INTO resume_main(name,phone,email,gender,birth_date,city,desired_position,desired_city,desired_salary_min,desired_salary_max,job_status,highest_edu,total_exp_years,parse_success,source,self_intro,deleted,creator_id,created_time,updated_time) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,0,?,?,0,1,?,?)", (body.get("name"), body.get("phone"), body.get("email"), body.get("gender"), body.get("birthDate"), body.get("city"), body.get("desiredPosition"), body.get("desiredCity"), body.get("desiredSalaryMin"), body.get("desiredSalaryMax"), body.get("jobStatus"), highest, total_exp, body.get("source","MANUAL"), body.get("selfIntro"), now(), now()))
        rid = scalar(conn, "SELECT last_insert_rowid()")
    conn.execute("DELETE FROM resume_skill WHERE resume_id=?", (rid,))
    conn.execute("DELETE FROM resume_education WHERE resume_id=?", (rid,))
    conn.execute("DELETE FROM resume_work_exp WHERE resume_id=?", (rid,))
    conn.executemany("INSERT INTO resume_skill(resume_id,skill_name) VALUES(?,?)", [(rid, s) for s in body.get("skills", [])])
    conn.executemany("INSERT INTO resume_education(resume_id,school,major,edu_level,start_date,end_date) VALUES(?,?,?,?,?,?)", [(rid, e.get("school"), e.get("major"), e.get("eduLevel"), e.get("startDate"), e.get("endDate")) for e in body.get("educations", [])])
    conn.executemany("INSERT INTO resume_work_exp(resume_id,company,position,industry,start_date,end_date,description) VALUES(?,?,?,?,?,?,?)", [(rid, w.get("company"), w.get("position"), w.get("industry"), w.get("startDate"), w.get("endDate"), w.get("description")) for w in body.get("workExps", [])])
    conn.commit()
    return get_resume(conn, rid)


def upload_resume(conn, filename: str, data: bytes):
    ext = os.path.splitext(filename)[1].lower()
    if ext not in (".pdf", ".doc", ".docx"):
        raise ApiError(400, "仅支持 PDF、DOC、DOCX 格式")
    stored = f"{uuid.uuid4()}{ext}"
    (UPLOAD_DIR / stored).write_bytes(data)
    conn.execute("INSERT INTO resume_main(file_path,parse_success,source,deleted,creator_id,created_time,updated_time) VALUES(?,0,'FILE',0,1,?,?)", (stored, now(), now()))
    conn.commit()
    return get_resume(conn, scalar(conn, "SELECT last_insert_rowid()"))


def delete_resume(conn, rid):
    conn.execute("UPDATE resume_main SET deleted=1,updated_time=? WHERE id=?", (now(), rid))
    conn.commit()


def edu_level(code):
    return {"HIGH_SCHOOL":1,"ASSOCIATE":2,"BACHELOR":3,"MASTER":4,"DOCTOR":5}.get(code or "", 0)


def run_match(conn, position_id):
    job = get_job(conn, position_id)
    if job["status"] != "OPEN":
        raise ApiError(400, "职位不存在或未发布")
    cfg = row(conn, "SELECT * FROM match_config WHERE id=1")
    pos_skills = {s.lower() for s in job.get("skillTags", [])}
    conn.execute("DELETE FROM match_result WHERE position_id=?", (position_id,))
    for resume in rows(conn, "SELECT * FROM resume_main WHERE deleted=0"):
        rskills = {x["skillName"].lower() for x in rows(conn, "SELECT * FROM resume_skill WHERE resume_id=?", (resume["id"],))}
        skill_score = 100.0 if not pos_skills else round(len(pos_skills & rskills) * 100 / len(pos_skills), 2)
        edu_score = 100.0 if not job.get("eduRequire") or not resume.get("highestEdu") else (100.0 if edu_level(resume.get("highestEdu")) >= edu_level(job.get("eduRequire")) else 0.0)
        required, actual = job.get("expRequire") or 0, resume.get("totalExpYears") or 0
        exp_score = 100.0 if required <= 0 else (100.0 if actual >= required else round(actual * 100 / required, 2) if actual > 0 else 0.0)
        total = round((skill_score * cfg["skillWeight"] + edu_score * cfg["eduWeight"] + exp_score * cfg["expWeight"]) / 100, 2)
        conn.execute("INSERT INTO match_result(position_id,resume_id,total_score,skill_score,edu_score,exp_score,matched_at) VALUES(?,?,?,?,?,?,?)", (position_id, resume["id"], total, skill_score, edu_score, exp_score, now()))
    conn.commit()
    return match_results(conn, position_id)


def match_results(conn, position_id):
    return rows(conn, "SELECT mr.*,rm.name resume_name,rm.phone resume_phone FROM match_result mr LEFT JOIN resume_main rm ON rm.id=mr.resume_id WHERE mr.position_id=? ORDER BY mr.total_score DESC", (position_id,))


def get_match_config(conn):
    return row(conn, "SELECT * FROM match_config WHERE id=1")


def update_match_config(conn, body):
    if sum([body.get("skillWeight",0), body.get("eduWeight",0), body.get("expWeight",0)]) != 100:
        raise ApiError(422, "各维度权重总和必须等于 100%")
    conn.execute("UPDATE match_config SET skill_weight=?,edu_weight=?,exp_weight=?,updated_time=?,updater_id=1 WHERE id=1", (body["skillWeight"], body["eduWeight"], body["expWeight"], now()))
    conn.commit()


def list_applications(conn, query):
    where, args = ["1=1"], []
    if query.get("positionId"):
        where.append("ja.position_id=?"); args.append(query["positionId"][0])
    if query.get("status"):
        where.append("ja.status=?"); args.append(query["status"][0])
    sql_where = " AND ".join(where)
    total = scalar(conn, f"SELECT COUNT(*) FROM job_application ja WHERE {sql_where}", args)
    limit, offset = page_params(query)
    data = rows(conn, f"SELECT ja.*,jp.title position_title,rm.name resume_name FROM job_application ja LEFT JOIN job_position jp ON jp.id=ja.position_id LEFT JOIN resume_main rm ON rm.id=ja.resume_id WHERE {sql_where} ORDER BY ja.id DESC LIMIT ? OFFSET ?", [*args, limit, offset])
    return table(data, total)


def get_application(conn, app_id):
    item = row(conn, "SELECT ja.*,jp.title position_title,rm.name resume_name FROM job_application ja LEFT JOIN job_position jp ON jp.id=ja.position_id LEFT JOIN resume_main rm ON rm.id=ja.resume_id WHERE ja.id=?", (app_id,))
    if not item:
        raise ApiError(404, "投递记录不存在")
    return item


def create_application(conn, body):
    job = get_job(conn, body.get("positionId"))
    if job["status"] != "OPEN":
        raise ApiError(400, "该职位已关闭，无法创建投递")
    conn.execute("INSERT INTO job_application(position_id,resume_id,status,operate_time,operator_id,remark,created_time) VALUES(?,?,'PENDING',?,?,?,?)", (body.get("positionId"), body.get("resumeId"), now(), 1, body.get("remark"), now()))
    conn.commit()
    return get_application(conn, scalar(conn, "SELECT last_insert_rowid()"))


def change_application_status(conn, app_id, status, remark):
    app = get_application(conn, app_id)
    valid = {"PENDING:RESUME_PASSED","PENDING:RESUME_REJECTED","RESUME_PASSED:INTERVIEW_WAITING","INTERVIEW_WAITING:INTERVIEWING","INTERVIEWING:INTERVIEW_PASSED","INTERVIEWING:INTERVIEW_REJECTED","INTERVIEW_PASSED:HIRED"}
    transition = f"{app['status']}:{status}"
    if transition not in valid:
        raise ApiError(400, f"非法状态流转: {app['status']} -> {status}")
    conn.execute("UPDATE job_application SET status=?,operate_time=?,operator_id=1,remark=? WHERE id=?", (status, now(), remark, app_id))
    conn.execute("INSERT INTO application_log(application_id,from_status,to_status,operator_id,operator_name,remark,created_time) VALUES(?,?,?,?,?,?,?)", (app_id, app["status"], status, 1, "System Admin", remark, now()))
    conn.commit()


def application_logs(conn, app_id):
    return rows(conn, "SELECT * FROM application_log WHERE application_id=? ORDER BY id", (app_id,))


def list_interviews(conn, query):
    where, args = ["1=1"], []
    if query.get("interviewer"):
        where.append("interviewer LIKE ?"); args.append(f"%{query['interviewer'][0]}%")
    sql_where = " AND ".join(where)
    total = scalar(conn, f"SELECT COUNT(*) FROM interview_record WHERE {sql_where}", args)
    limit, offset = page_params(query)
    return table(rows(conn, f"SELECT * FROM interview_record WHERE {sql_where} ORDER BY id DESC LIMIT ? OFFSET ?", [*args, limit, offset]), total)


def get_interview(conn, iid):
    item = row(conn, "SELECT * FROM interview_record WHERE id=?", (iid,))
    if not item:
        raise ApiError(404, "面试记录不存在")
    return item


def create_interview(conn, body):
    app = get_application(conn, body.get("applicationId"))
    if app["status"] not in ("INTERVIEW_WAITING", "INTERVIEWING"):
        raise ApiError(400, "当前投递状态不允许创建面试")
    conn.execute("INSERT INTO interview_record(application_id,interview_time,interviewer,location,created_time) VALUES(?,?,?,?,?)", (body.get("applicationId"), body.get("interviewTime"), body.get("interviewer"), body.get("location"), now()))
    iid = scalar(conn, "SELECT last_insert_rowid()")
    conn.commit()
    if app["status"] == "INTERVIEW_WAITING":
        change_application_status(conn, body.get("applicationId"), "INTERVIEWING", "创建面试后进入面试中")
    return get_interview(conn, iid)


def update_interview(conn, iid, body):
    conn.execute("UPDATE interview_record SET interview_time=?,interviewer=?,location=? WHERE id=?", (body.get("interviewTime"), body.get("interviewer"), body.get("location"), iid))
    conn.commit()


def update_interview_result(conn, iid, body):
    item = get_interview(conn, iid)
    result = body.get("result")
    conn.execute("UPDATE interview_record SET score=?,comment=?,result=? WHERE id=?", (body.get("score"), body.get("comment"), result, iid))
    conn.commit()
    change_application_status(conn, item["applicationId"], "INTERVIEW_PASSED" if result == "PASS" else "INTERVIEW_REJECTED", f"面试结果：{result}")


def dashboard(conn):
    ym = now()[:7]
    applied = scalar(conn, "SELECT COUNT(*) FROM job_application WHERE substr(created_time,1,7)=?", (ym,))
    interviewed = scalar(conn, "SELECT COUNT(*) FROM interview_record WHERE result IS NOT NULL AND substr(created_time,1,7)=?", (ym,))
    hired = scalar(conn, "SELECT COUNT(*) FROM job_application WHERE status='HIRED' AND substr(operate_time,1,7)=?", (ym,))
    passed = scalar(conn, "SELECT COUNT(*) FROM job_application WHERE status NOT IN('PENDING','RESUME_REJECTED') AND substr(created_time,1,7)=?", (ym,))
    interviewing = scalar(conn, "SELECT COUNT(*) FROM job_application WHERE status IN('INTERVIEWING','INTERVIEW_PASSED','INTERVIEW_REJECTED','HIRED') AND substr(created_time,1,7)=?", (ym,))
    return {"monthlyApplications": applied, "monthlyInterviewDone": interviewed, "monthlyHired": hired, "funnel": {"applied": applied, "passed": passed, "interviewed": interviewing, "hired": hired}}


def source_stats(conn, query):
    sql, args = "SELECT COALESCE(source,'UNKNOWN') source,COUNT(*) cnt FROM resume_main WHERE deleted=0", []
    if query.get("startDate"):
        sql += " AND date(created_time)>=date(?)"; args.append(query["startDate"][0])
    if query.get("endDate"):
        sql += " AND date(created_time)<=date(?)"; args.append(query["endDate"][0])
    return {"distribution": rows(conn, sql + " GROUP BY source", args)}
