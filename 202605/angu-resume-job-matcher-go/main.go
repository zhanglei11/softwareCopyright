package main

import (
	"database/sql"
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"
	"strings"

	"angu-resume-job-matcher-go/internal/api"
	"angu-resume-job-matcher-go/internal/db"
	"angu-resume-job-matcher-go/internal/service"
	"angu-resume-job-matcher-go/internal/swagger"
)

type App struct {
	db *sql.DB
}

func main() {
	database, err := db.Open()
	if err != nil {
		panic(err)
	}
	app := &App{db: database}
	mux := http.NewServeMux()
	mux.HandleFunc("/", app.handle)
	fmt.Println("Go backend listening on http://127.0.0.1:19915")
	if err := http.ListenAndServe("127.0.0.1:19915", cors(mux)); err != nil {
		panic(err)
	}
}

func cors(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type,Authorization")
		if r.Method == http.MethodOptions {
			w.WriteHeader(http.StatusNoContent)
			return
		}
		next.ServeHTTP(w, r)
	})
}

func (a *App) handle(w http.ResponseWriter, r *http.Request) {
	if r.URL.Path == "/v3/api-docs" {
		w.Header().Set("Content-Type", "application/json; charset=utf-8")
		_, _ = w.Write(swagger.Spec())
		return
	}
	if r.URL.Path == "/swagger-ui.html" || r.URL.Path == "/swagger-ui" {
		w.Header().Set("Content-Type", "text/html; charset=utf-8")
		_, _ = w.Write([]byte(swagger.HTML()))
		return
	}
	data, err := a.dispatch(r)
	if err != nil {
		if e, ok := err.(api.APIError); ok {
			api.WriteJSON(w, api.Error(e.Code, e.Message))
			return
		}
		api.WriteJSON(w, api.Error(500, err.Error()))
		return
	}
	api.WriteJSON(w, api.Ok(data))
}

func (a *App) dispatch(r *http.Request) (interface{}, error) {
	path, method := r.URL.Path, r.Method
	body, _ := api.ReadJSON(r)
	limit, offset := api.Page(r)
	if method == "POST" && path == "/api/auth/login" {
		return service.Login(a.db, body)
	}
	if method == "POST" && path == "/api/auth/refresh" {
		claims := api.ParseToken(api.Str(body, "refreshToken"))
		if claims == nil || claims["type"] != "refresh" {
			return nil, api.APIError{Code: 401, Message: "refreshToken 已失效，请重新登录"}
		}
		username, _ := claims["sub"].(string)
		userID := int64(1)
		if v, ok := claims["user_id"].(float64); ok { userID = int64(v) }
		return map[string]interface{}{"accessToken": api.Token(username, userID, "access", 2*60*60*1000000000), "refreshToken": api.Token(username, userID, "refresh", 7*24*60*60*1000000000), "userId": userID, "username": username, "realName": nil}, nil
	}
	if method == "POST" && path == "/api/auth/logout" {
		return nil, nil
	}
	if method == "GET" && path == "/api/auth/me" {
		return service.GetUser(a.db, 1)
	}
	if method == "GET" && (path == "/api/system/menus/tree" || path == "/api/system/menus/my-tree") {
		return service.MenuTree(a.db), nil
	}
	if method == "POST" && path == "/api/system/menus" {
		id := api.Int(body, "id")
		if id == 0 { id = db.ScalarInt(a.db, "SELECT COALESCE(MAX(id),0)+1 FROM sys_menu") }
		_, err := a.db.Exec("INSERT INTO sys_menu(id,parent_id,menu_type,menu_name,path,perm_code,icon,sort) VALUES(?,?,?,?,?,?,?,?)", id, api.Int(body,"parentId"), api.Int(body,"menuType"), api.Str(body,"menuName"), api.Str(body,"path"), api.Str(body,"permCode"), api.Str(body,"icon"), api.Int(body,"sort"))
		return nil, err
	}
	if id, ok := match(path, "/api/system/menus/"); ok {
		if method == "PUT" { _, err := a.db.Exec("UPDATE sys_menu SET parent_id=?,menu_type=?,menu_name=?,path=?,perm_code=?,icon=?,sort=? WHERE id=?", api.Int(body,"parentId"), api.Int(body,"menuType"), api.Str(body,"menuName"), api.Str(body,"path"), api.Str(body,"permCode"), api.Str(body,"icon"), api.Int(body,"sort"), id); return nil, err }
		if method == "DELETE" { _, err := a.db.Exec("DELETE FROM sys_menu WHERE id=?", id); return nil, err }
	}
	if method == "GET" && path == "/api/system/users" {
		return service.ListUsers(a.db, limit, offset, r.URL.Query()), nil
	}
	if method == "POST" && path == "/api/system/users" {
		return nil, service.CreateUser(a.db, body)
	}
	if id, ok := match(path, "/api/system/users/"); ok {
		if method == "GET" { return service.GetUser(a.db, id) }
		if method == "PUT" { return nil, service.UpdateUser(a.db, id, body) }
		if method == "DELETE" { _, err := a.db.Exec("UPDATE sys_user SET deleted=1 WHERE id=?", id); return nil, err }
	}
	if strings.HasPrefix(path, "/api/system/users/") {
		rest := strings.TrimPrefix(path, "/api/system/users/"); parts := strings.Split(rest, "/"); id,_ := strconv.ParseInt(parts[0],10,64)
		if len(parts)==2 && parts[1]=="reset-password" && method=="PUT" { _, err := a.db.Exec("UPDATE sys_user SET password=?,updated_time=? WHERE id=?", api.Str(body,"newPassword"), api.Now(), id); return nil, err }
		if len(parts)==2 && parts[1]=="status" && method=="PUT" { _, err := a.db.Exec("UPDATE sys_user SET status=?,updated_time=? WHERE id=?", api.Int(body,"status"), api.Now(), id); return nil, err }
	}
	if method == "GET" && path == "/api/system/roles" {
		return service.Roles(a.db), nil
	}
	if method == "POST" && path == "/api/system/roles" {
		_, err := a.db.Exec("INSERT INTO sys_role(role_name,role_code,builtin,status,remark,created_time,updated_time) VALUES(?,?,0,1,?,?,?)", api.Str(body,"roleName"), api.Str(body,"roleCode"), api.Str(body,"remark"), api.Now(), api.Now())
		return nil, err
	}
	if strings.HasPrefix(path, "/api/system/roles/") {
		rest := strings.TrimPrefix(path, "/api/system/roles/"); parts := strings.Split(rest, "/"); id,_ := strconv.ParseInt(parts[0],10,64)
		if len(parts)==1 && method=="PUT" { _, err := a.db.Exec("UPDATE sys_role SET role_name=?,role_code=?,status=?,remark=?,updated_time=? WHERE id=?", api.Str(body,"roleName"), api.Str(body,"roleCode"), api.Int(body,"status"), api.Str(body,"remark"), api.Now(), id); return nil, err }
		if len(parts)==1 && method=="DELETE" { _, err := a.db.Exec("DELETE FROM sys_role WHERE id=?", id); return nil, err }
		if len(parts)==2 && parts[1]=="menus" && method=="PUT" { _, _ = a.db.Exec("DELETE FROM sys_role_menu WHERE role_id=?", id); for _, mid := range api.IDs(body,"menuIds") { _, _ = a.db.Exec("INSERT OR IGNORE INTO sys_role_menu(role_id,menu_id) VALUES(?,?)", id, mid) }; return nil, nil }
	}
	if method == "GET" && path == "/api/jobs" {
		return service.ListJobs(a.db, limit, offset, r.URL.Query()), nil
	}
	if method == "POST" && path == "/api/jobs" {
		return nil, service.SaveJob(a.db, body, 0)
	}
	if strings.HasPrefix(path, "/api/jobs/") {
		rest := strings.TrimPrefix(path, "/api/jobs/")
		parts := strings.Split(rest, "/")
		id, _ := strconv.ParseInt(parts[0], 10, 64)
		if len(parts) == 2 && method == "PUT" && parts[1] == "publish" { return nil, service.SetJobStatus(a.db, id, "OPEN") }
		if len(parts) == 2 && method == "PUT" && parts[1] == "close" { return nil, service.SetJobStatus(a.db, id, "CLOSED") }
		if method == "GET" { return service.GetJob(a.db, id) }
		if method == "PUT" { return nil, service.SaveJob(a.db, body, id) }
		if method == "DELETE" { return nil, service.DeleteJob(a.db, id) }
	}
	if method == "GET" && path == "/api/resumes" { return service.ListResumes(a.db, limit, offset, r.URL.Query()), nil }
	if method == "POST" && path == "/api/resumes" { return service.SaveResume(a.db, body, 0) }
	if strings.HasPrefix(path, "/api/resumes/") {
		id, _ := strconv.ParseInt(strings.TrimPrefix(path, "/api/resumes/"), 10, 64)
		if method == "GET" { return service.GetResume(a.db, id) }
		if method == "PUT" { return service.SaveResume(a.db, body, id) }
		if method == "DELETE" { _, err := a.db.Exec("UPDATE resume_main SET deleted=1 WHERE id=?", id); return nil, err }
	}
	if method == "POST" && path == "/api/match/run" { return service.RunMatch(a.db, api.Int(body, "positionId")) }
	if strings.HasPrefix(path, "/api/match/results/") && method == "GET" {
		id, _ := strconv.ParseInt(strings.TrimPrefix(path, "/api/match/results/"), 10, 64); return service.MatchResults(a.db, id), nil
	}
	if path == "/api/match/config" {
		if method == "GET" { return db.One(a.db, "SELECT * FROM match_config WHERE id=1"), nil }
		if method == "PUT" { _, err := a.db.Exec("UPDATE match_config SET skill_weight=?,edu_weight=?,exp_weight=?,updated_time=? WHERE id=1", api.Int(body,"skillWeight"), api.Int(body,"eduWeight"), api.Int(body,"expWeight"), api.Now()); return nil, err }
	}
	if method == "GET" && path == "/api/applications" { return service.ListApplications(a.db, limit, offset, r.URL.Query()), nil }
	if method == "POST" && path == "/api/applications" { return service.CreateApplication(a.db, body) }
	if strings.HasPrefix(path, "/api/applications/") {
		rest := strings.TrimPrefix(path, "/api/applications/"); parts := strings.Split(rest, "/"); id, _ := strconv.ParseInt(parts[0], 10, 64)
		if len(parts)==1 && method=="GET" { return service.GetApplication(a.db, id) }
		if len(parts)==2 && parts[1]=="status" && method=="PUT" { return nil, service.ChangeApplicationStatus(a.db, id, api.Str(body,"status"), api.Str(body,"remark")) }
		if len(parts)==2 && parts[1]=="logs" && method=="GET" { rows,_ := db.Query(a.db, "SELECT * FROM application_log WHERE application_id=? ORDER BY id", id); return rows,nil }
	}
	if method == "GET" && path == "/api/interviews" { return service.ListInterviews(a.db, limit, offset, r.URL.Query()), nil }
	if method == "POST" && path == "/api/interviews" { return service.CreateInterview(a.db, body) }
	if strings.HasPrefix(path, "/api/interviews/") {
		rest := strings.TrimPrefix(path, "/api/interviews/"); parts := strings.Split(rest, "/"); id,_ := strconv.ParseInt(parts[0],10,64)
		if len(parts)==1 && method=="GET" { return db.One(a.db, "SELECT * FROM interview_record WHERE id=?", id), nil }
		if len(parts)==2 && parts[1]=="result" && method=="PUT" {
			_,err := a.db.Exec("UPDATE interview_record SET score=?,comment=?,result=? WHERE id=?", api.Int(body,"score"), api.Str(body,"comment"), api.Str(body,"result"), id)
			if err != nil { return nil, err }
			rec := db.One(a.db, "SELECT * FROM interview_record WHERE id=?", id)
			next := "INTERVIEW_REJECTED"
			if api.Str(body,"result") == "PASS" { next = "INTERVIEW_PASSED" }
			return nil, service.ChangeApplicationStatus(a.db, rec["applicationId"].(int64), next, "面试结果："+api.Str(body,"result"))
		}
	}
	if method == "GET" && path == "/api/stats/dashboard" { return service.Dashboard(a.db), nil }
	if method == "GET" && path == "/api/stats/source" { rows,_ := db.Query(a.db, "SELECT COALESCE(source,'UNKNOWN') source,COUNT(*) cnt FROM resume_main WHERE deleted=0 GROUP BY source"); return map[string]interface{}{"distribution": rows}, nil }
	return nil, api.APIError{Code: 404, Message: "接口不存在"}
}

func match(path, prefix string) (int64, bool) {
	if !strings.HasPrefix(path, prefix) { return 0, false }
	rest := strings.TrimPrefix(path, prefix)
	if strings.Contains(rest, "/") { return 0, false }
	id, err := strconv.ParseInt(rest, 10, 64)
	return id, err == nil
}

func pretty(v interface{}) string {
	b, _ := json.MarshalIndent(v, "", "  ")
	return string(b)
}
