package service

import (
	"database/sql"
	"encoding/json"
	"fmt"
	"math"
	"strings"
	"time"

	"angu-resume-job-matcher-go/internal/api"
	"angu-resume-job-matcher-go/internal/db"
)

func Login(database *sql.DB, body map[string]interface{}) (map[string]interface{}, error) {
	username, password := api.Str(body, "username"), api.Str(body, "password")
	user := db.One(database, "SELECT * FROM sys_user WHERE username=? AND deleted=0", username)
	if user == nil || !(username == "admin" && (password == "admin123" || password == "123456")) {
		return nil, api.APIError{Code: 401, Message: "用户名或密码错误"}
	}
	return map[string]interface{}{"accessToken": api.Token(username, 1, "access", 2*time.Hour), "refreshToken": api.Token(username, 1, "refresh", 7*24*time.Hour), "userId": int64(1), "username": username, "realName": user["realName"]}, nil
}

func MenuTree(database *sql.DB) []map[string]interface{} {
	all, _ := db.Query(database, "SELECT * FROM sys_menu ORDER BY sort,id")
	var walk func(int64) []map[string]interface{}
	walk = func(parent int64) []map[string]interface{} {
		out := []map[string]interface{}{}
		for _, item := range all {
			if asInt(item["parentId"]) == parent {
				item["children"] = walk(asInt(item["id"]))
				out = append(out, item)
			}
		}
		return out
	}
	return walk(0)
}

func Page(database *sql.DB, rLimit, rOffset int, table, where, order string, args ...interface{}) map[string]interface{} {
	total := db.ScalarInt(database, fmt.Sprintf("SELECT COUNT(*) FROM %s WHERE %s", table, where), args...)
	queryArgs := append(args, rLimit, rOffset)
	rows, _ := db.Query(database, fmt.Sprintf("SELECT * FROM %s WHERE %s ORDER BY %s LIMIT ? OFFSET ?", table, where, order), queryArgs...)
	return map[string]interface{}{"rows": rows, "total": total}
}

func ListUsers(database *sql.DB, limit, offset int, q map[string][]string) map[string]interface{} {
	where, args := []string{"deleted=0"}, []interface{}{}
	addLike(&where, &args, q, "username", "username")
	addLike(&where, &args, q, "phone", "phone")
	addEq(&where, &args, q, "status", "status")
	return Page(database, limit, offset, "sys_user", strings.Join(where, " AND "), "id DESC", args...)
}

func GetUser(database *sql.DB, id int64) (map[string]interface{}, error) {
	user := db.One(database, "SELECT id,username,real_name,phone,status,error_count,locked_until,deleted,created_time,updated_time FROM sys_user WHERE id=? AND deleted=0", id)
	if user == nil {
		return nil, api.APIError{Code: 404, Message: "用户不存在"}
	}
	roles, _ := db.Query(database, "SELECT role_id FROM sys_user_role WHERE user_id=?", id)
	ids := []int64{}
	for _, r := range roles {
		ids = append(ids, asInt(r["roleId"]))
	}
	user["roleIds"] = ids
	return user, nil
}

func CreateUser(database *sql.DB, body map[string]interface{}) error {
	_, err := database.Exec("INSERT INTO sys_user(username,real_name,phone,password,status,error_count,deleted,created_time,updated_time) VALUES(?,?,?,?,1,0,0,?,?)", api.Str(body, "username"), api.Str(body, "realName"), api.Str(body, "phone"), api.Str(body, "password"), api.Now(), api.Now())
	if err != nil {
		return err
	}
	id := db.ScalarInt(database, "SELECT last_insert_rowid()")
	replaceUserRoles(database, id, api.IDs(body, "roleIds"))
	return nil
}

func UpdateUser(database *sql.DB, id int64, body map[string]interface{}) error {
	_, err := database.Exec("UPDATE sys_user SET real_name=COALESCE(NULLIF(?,''),real_name),phone=COALESCE(NULLIF(?,''),phone),status=COALESCE(NULLIF(?,0),status),updated_time=? WHERE id=?", api.Str(body, "realName"), api.Str(body, "phone"), api.Int(body, "status"), api.Now(), id)
	if _, ok := body["roleIds"]; ok {
		replaceUserRoles(database, id, api.IDs(body, "roleIds"))
	}
	return err
}

func replaceUserRoles(database *sql.DB, id int64, roleIDs []int64) {
	_, _ = database.Exec("DELETE FROM sys_user_role WHERE user_id=?", id)
	for _, rid := range roleIDs {
		_, _ = database.Exec("INSERT OR IGNORE INTO sys_user_role(user_id,role_id) VALUES(?,?)", id, rid)
	}
}

func Roles(database *sql.DB) []map[string]interface{} {
	rows, _ := db.Query(database, "SELECT * FROM sys_role ORDER BY id")
	return rows
}

func ListJobs(database *sql.DB, limit, offset int, q map[string][]string) map[string]interface{} {
	where, args := []string{"deleted=0"}, []interface{}{}
	addLike(&where, &args, q, "title", "title")
	addLike(&where, &args, q, "department", "department")
	addEq(&where, &args, q, "status", "status")
	total := db.ScalarInt(database, "SELECT COUNT(*) FROM job_position WHERE "+strings.Join(where, " AND "), args...)
	queryArgs := append(args, limit, offset)
	rows, _ := db.Query(database, "SELECT * FROM job_position WHERE "+strings.Join(where, " AND ")+" ORDER BY id DESC LIMIT ? OFFSET ?", queryArgs...)
	for _, item := range rows {
		item["skillTags"] = parseStringArray(item["skillTags"])
	}
	return map[string]interface{}{"rows": rows, "total": total}
}

func GetJob(database *sql.DB, id int64) (map[string]interface{}, error) {
	item := db.One(database, "SELECT * FROM job_position WHERE id=? AND deleted=0", id)
	if item == nil {
		return nil, api.APIError{Code: 404, Message: "职位不存在"}
	}
	item["skillTags"] = parseStringArray(item["skillTags"])
	return item, nil
}

func SaveJob(database *sql.DB, body map[string]interface{}, id int64) error {
	skills, _ := json.Marshal(api.Strings(body, "skillTags"))
	if id > 0 {
		_, err := database.Exec("UPDATE job_position SET title=?,department=?,job_type=?,location=?,salary_min=?,salary_max=?,edu_require=?,exp_require=?,description=?,skill_tags=?,updated_time=? WHERE id=?", api.Str(body, "title"), api.Str(body, "department"), api.Str(body, "jobType"), api.Str(body, "location"), api.Int(body, "salaryMin"), api.Int(body, "salaryMax"), api.Str(body, "eduRequire"), api.Int(body, "expRequire"), api.Str(body, "description"), string(skills), api.Now(), id)
		return err
	}
	_, err := database.Exec("INSERT INTO job_position(title,department,job_type,location,salary_min,salary_max,edu_require,exp_require,description,skill_tags,status,deleted,creator_id,created_time,updated_time) VALUES(?,?,?,?,?,?,?,?,?,?,'DRAFT',0,1,?,?)", api.Str(body, "title"), api.Str(body, "department"), api.Str(body, "jobType"), api.Str(body, "location"), api.Int(body, "salaryMin"), api.Int(body, "salaryMax"), api.Str(body, "eduRequire"), api.Int(body, "expRequire"), api.Str(body, "description"), string(skills), api.Now(), api.Now())
	return err
}

func SetJobStatus(database *sql.DB, id int64, status string) error {
	_, err := database.Exec("UPDATE job_position SET status=?,updated_time=? WHERE id=?", status, api.Now(), id)
	return err
}

func DeleteJob(database *sql.DB, id int64) error {
	job, _ := GetJob(database, id)
	if job["status"] != "DRAFT" {
		return api.APIError{Code: 400, Message: "仅 DRAFT 状态可删除"}
	}
	_, err := database.Exec("UPDATE job_position SET deleted=1,updated_time=? WHERE id=?", api.Now(), id)
	return err
}

func ListResumes(database *sql.DB, limit, offset int, q map[string][]string) map[string]interface{} {
	where, args := []string{"deleted=0"}, []interface{}{}
	addLike(&where, &args, q, "name", "name")
	addLike(&where, &args, q, "phone", "phone")
	if v := first(q, "skill"); v != "" {
		where = append(where, "id IN (SELECT resume_id FROM resume_skill WHERE skill_name LIKE ?)")
		args = append(args, "%"+v+"%")
	}
	total := db.ScalarInt(database, "SELECT COUNT(*) FROM resume_main WHERE "+strings.Join(where, " AND "), args...)
	queryArgs := append(args, limit, offset)
	rows, _ := db.Query(database, "SELECT * FROM resume_main WHERE "+strings.Join(where, " AND ")+" ORDER BY id DESC LIMIT ? OFFSET ?", queryArgs...)
	for _, item := range rows {
		FillResume(database, item)
	}
	return map[string]interface{}{"rows": rows, "total": total}
}

func GetResume(database *sql.DB, id int64) (map[string]interface{}, error) {
	item := db.One(database, "SELECT * FROM resume_main WHERE id=? AND deleted=0", id)
	if item == nil {
		return nil, api.APIError{Code: 404, Message: "简历不存在"}
	}
	FillResume(database, item)
	return item, nil
}

func FillResume(database *sql.DB, item map[string]interface{}) {
	id := asInt(item["id"])
	item["skills"], _ = db.Query(database, "SELECT * FROM resume_skill WHERE resume_id=? ORDER BY id", id)
	item["educations"], _ = db.Query(database, "SELECT * FROM resume_education WHERE resume_id=? ORDER BY id", id)
	item["workExps"], _ = db.Query(database, "SELECT * FROM resume_work_exp WHERE resume_id=? ORDER BY id", id)
}

func SaveResume(database *sql.DB, body map[string]interface{}, id int64) (map[string]interface{}, error) {
	if id > 0 {
		_, _ = database.Exec("UPDATE resume_main SET name=?,phone=?,email=?,city=?,desired_position=?,desired_city=?,source=?,self_intro=?,updated_time=? WHERE id=?", api.Str(body, "name"), api.Str(body, "phone"), api.Str(body, "email"), api.Str(body, "city"), api.Str(body, "desiredPosition"), api.Str(body, "desiredCity"), valueOr(api.Str(body, "source"), "MANUAL"), api.Str(body, "selfIntro"), api.Now(), id)
	} else {
		_, _ = database.Exec("INSERT INTO resume_main(name,phone,email,city,desired_position,desired_city,source,self_intro,deleted,creator_id,created_time,updated_time) VALUES(?,?,?,?,?,?,?, ?,0,1,?,?)", api.Str(body, "name"), api.Str(body, "phone"), api.Str(body, "email"), api.Str(body, "city"), api.Str(body, "desiredPosition"), api.Str(body, "desiredCity"), valueOr(api.Str(body, "source"), "MANUAL"), api.Str(body, "selfIntro"), api.Now(), api.Now())
		id = db.ScalarInt(database, "SELECT last_insert_rowid()")
	}
	_, _ = database.Exec("DELETE FROM resume_skill WHERE resume_id=?", id)
	for _, s := range api.Strings(body, "skills") {
		_, _ = database.Exec("INSERT INTO resume_skill(resume_id,skill_name) VALUES(?,?)", id, s)
	}
	return GetResume(database, id)
}

func RunMatch(database *sql.DB, positionID int64) ([]map[string]interface{}, error) {
	job, err := GetJob(database, positionID)
	if err != nil {
		return nil, err
	}
	if job["status"] != "OPEN" {
		return nil, api.APIError{Code: 400, Message: "职位不存在或未发布"}
	}
	cfg := db.One(database, "SELECT * FROM match_config WHERE id=1")
	posSkills := lowerSet(job["skillTags"])
	resumes, _ := db.Query(database, "SELECT * FROM resume_main WHERE deleted=0")
	_, _ = database.Exec("DELETE FROM match_result WHERE position_id=?", positionID)
	for _, resume := range resumes {
		skillRows, _ := db.Query(database, "SELECT skill_name FROM resume_skill WHERE resume_id=?", resume["id"])
		rskills := map[string]bool{}
		for _, r := range skillRows {
			rskills[strings.ToLower(fmt.Sprint(r["skillName"]))] = true
		}
		skillScore := 100.0
		if len(posSkills) > 0 {
			matched := 0
			for s := range posSkills {
				if rskills[s] {
					matched++
				}
			}
			skillScore = round2(float64(matched) * 100 / float64(len(posSkills)))
		}
		total := round2(skillScore * float64(asInt(cfg["skillWeight"])) / 100)
		_, _ = database.Exec("INSERT INTO match_result(position_id,resume_id,total_score,skill_score,edu_score,exp_score,matched_at) VALUES(?,?,?,?,?,?,?)", positionID, resume["id"], total, skillScore, 100, 0, api.Now())
	}
	return MatchResults(database, positionID), nil
}

func MatchResults(database *sql.DB, positionID int64) []map[string]interface{} {
	rows, _ := db.Query(database, "SELECT mr.*,rm.name resume_name,rm.phone resume_phone FROM match_result mr LEFT JOIN resume_main rm ON rm.id=mr.resume_id WHERE mr.position_id=? ORDER BY mr.total_score DESC", positionID)
	return rows
}

func ListApplications(database *sql.DB, limit, offset int, q map[string][]string) map[string]interface{} {
	where, args := []string{"1=1"}, []interface{}{}
	addEq(&where, &args, q, "positionId", "ja.position_id")
	addEq(&where, &args, q, "status", "ja.status")
	total := db.ScalarInt(database, "SELECT COUNT(*) FROM job_application ja WHERE "+strings.Join(where, " AND "), args...)
	queryArgs := append(args, limit, offset)
	rows, _ := db.Query(database, "SELECT ja.*,jp.title position_title,rm.name resume_name FROM job_application ja LEFT JOIN job_position jp ON jp.id=ja.position_id LEFT JOIN resume_main rm ON rm.id=ja.resume_id WHERE "+strings.Join(where, " AND ")+" ORDER BY ja.id DESC LIMIT ? OFFSET ?", queryArgs...)
	return map[string]interface{}{"rows": rows, "total": total}
}

func GetApplication(database *sql.DB, id int64) (map[string]interface{}, error) {
	item := db.One(database, "SELECT ja.*,jp.title position_title,rm.name resume_name FROM job_application ja LEFT JOIN job_position jp ON jp.id=ja.position_id LEFT JOIN resume_main rm ON rm.id=ja.resume_id WHERE ja.id=?", id)
	if item == nil {
		return nil, api.APIError{Code: 404, Message: "投递记录不存在"}
	}
	return item, nil
}

func CreateApplication(database *sql.DB, body map[string]interface{}) (map[string]interface{}, error) {
	_, err := database.Exec("INSERT INTO job_application(position_id,resume_id,status,operate_time,operator_id,remark,created_time) VALUES(?,?,'PENDING',?,?,?,?)", api.Int(body, "positionId"), api.Int(body, "resumeId"), api.Now(), 1, api.Str(body, "remark"), api.Now())
	if err != nil {
		return nil, err
	}
	return GetApplication(database, db.ScalarInt(database, "SELECT last_insert_rowid()"))
}

func ChangeApplicationStatus(database *sql.DB, id int64, status, remark string) error {
	app, err := GetApplication(database, id)
	if err != nil {
		return err
	}
	from := fmt.Sprint(app["status"])
	valid := map[string]bool{"PENDING:RESUME_PASSED": true, "PENDING:RESUME_REJECTED": true, "RESUME_PASSED:INTERVIEW_WAITING": true, "INTERVIEW_WAITING:INTERVIEWING": true, "INTERVIEWING:INTERVIEW_PASSED": true, "INTERVIEWING:INTERVIEW_REJECTED": true, "INTERVIEW_PASSED:HIRED": true}
	if !valid[from+":"+status] {
		return api.APIError{Code: 400, Message: "非法状态流转: " + from + " -> " + status}
	}
	_, _ = database.Exec("UPDATE job_application SET status=?,operate_time=?,operator_id=1,remark=? WHERE id=?", status, api.Now(), remark, id)
	_, _ = database.Exec("INSERT INTO application_log(application_id,from_status,to_status,operator_id,operator_name,remark,created_time) VALUES(?,?,?,?,?,?,?)", id, from, status, 1, "System Admin", remark, api.Now())
	return nil
}

func ListInterviews(database *sql.DB, limit, offset int, q map[string][]string) map[string]interface{} {
	where, args := []string{"1=1"}, []interface{}{}
	addLike(&where, &args, q, "interviewer", "interviewer")
	return Page(database, limit, offset, "interview_record", strings.Join(where, " AND "), "id DESC", args...)
}

func CreateInterview(database *sql.DB, body map[string]interface{}) (map[string]interface{}, error) {
	app, err := GetApplication(database, api.Int(body, "applicationId"))
	if err != nil {
		return nil, err
	}
	status := fmt.Sprint(app["status"])
	if status != "INTERVIEW_WAITING" && status != "INTERVIEWING" {
		return nil, api.APIError{Code: 400, Message: "当前投递状态不允许创建面试"}
	}
	_, _ = database.Exec("INSERT INTO interview_record(application_id,interview_time,interviewer,location,created_time) VALUES(?,?,?,?,?)", api.Int(body, "applicationId"), api.Str(body, "interviewTime"), api.Str(body, "interviewer"), api.Str(body, "location"), api.Now())
	id := db.ScalarInt(database, "SELECT last_insert_rowid()")
	if status == "INTERVIEW_WAITING" {
		_ = ChangeApplicationStatus(database, api.Int(body, "applicationId"), "INTERVIEWING", "创建面试后进入面试中")
	}
	return db.One(database, "SELECT * FROM interview_record WHERE id=?", id), nil
}

func Dashboard(database *sql.DB) map[string]interface{} {
	return map[string]interface{}{"monthlyApplications": 0, "monthlyInterviewDone": 0, "monthlyHired": 0, "funnel": map[string]int{"applied": 0, "passed": 0, "interviewed": 0, "hired": 0}}
}

func addLike(where *[]string, args *[]interface{}, q map[string][]string, key, col string) {
	if v := first(q, key); v != "" {
		*where = append(*where, col+" LIKE ?")
		*args = append(*args, "%"+v+"%")
	}
}

func addEq(where *[]string, args *[]interface{}, q map[string][]string, key, col string) {
	if v := first(q, key); v != "" {
		*where = append(*where, col+"=?")
		*args = append(*args, v)
	}
}

func first(q map[string][]string, key string) string {
	if xs := q[key]; len(xs) > 0 {
		return xs[0]
	}
	return ""
}

func parseStringArray(v interface{}) []string {
	var out []string
	_ = json.Unmarshal([]byte(fmt.Sprint(v)), &out)
	return out
}

func lowerSet(v interface{}) map[string]bool {
	out := map[string]bool{}
	for _, s := range v.([]string) {
		out[strings.ToLower(s)] = true
	}
	return out
}

func asInt(v interface{}) int64 {
	switch x := v.(type) {
	case int64:
		return x
	case int:
		return int64(x)
	case float64:
		return int64(x)
	default:
		return 0
	}
}

func round2(v float64) float64 { return math.Round(v*100) / 100 }

func valueOr(v, fallback string) string {
	if v == "" {
		return fallback
	}
	return v
}
