import csv
import io
import json
import re
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from urllib.parse import parse_qs, urlparse

from .common import ApiError, error, ok, parse_token
from .db import UPLOAD_DIR, connect
from . import services as svc
from .swagger import openapi, swagger_html


class Handler(BaseHTTPRequestHandler):
    def end_headers(self):
        self.send_header("Access-Control-Allow-Origin", "*")
        self.send_header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
        self.send_header("Access-Control-Allow-Headers", "Content-Type,Authorization")
        super().end_headers()

    def do_OPTIONS(self):
        self.send_response(204)
        self.end_headers()

    def do_HEAD(self):
        if self.path in ("/swagger-ui.html", "/swagger-ui", "/v3/api-docs"):
            self.send_response(200)
            self.send_header("Content-Type", "text/html; charset=utf-8" if "swagger" in self.path else "application/json; charset=utf-8")
            self.end_headers()
        else:
            self.send_error(404)

    def do_GET(self): self.handle_request()
    def do_POST(self): self.handle_request()
    def do_PUT(self): self.handle_request()
    def do_DELETE(self): self.handle_request()

    def json_body(self):
        length = int(self.headers.get("Content-Length", "0"))
        if not length:
            return {}
        data = self.rfile.read(length)
        if "multipart/form-data" in self.headers.get("Content-Type", ""):
            return data
        return json.loads(data.decode() or "{}")

    def write_json(self, payload, status=200):
        raw = json.dumps(payload, ensure_ascii=False).encode()
        self.send_response(status)
        self.send_header("Content-Type", "application/json; charset=utf-8")
        self.send_header("Content-Length", str(len(raw)))
        self.end_headers()
        self.wfile.write(raw)

    def write_bytes(self, data: bytes, content_type: str, filename: str | None = None):
        self.send_response(200)
        self.send_header("Content-Type", content_type)
        if filename:
            self.send_header("Content-Disposition", f'attachment; filename="{filename}"')
        self.send_header("Content-Length", str(len(data)))
        self.end_headers()
        self.wfile.write(data)

    def current_user_id(self):
        auth = self.headers.get("Authorization", "")
        if auth.startswith("Bearer "):
            claims = parse_token(auth[7:])
            if claims:
                return claims.get("user_id", 1)
        return 1

    def handle_request(self):
        parsed = urlparse(self.path)
        path, query, method = parsed.path, parse_qs(parsed.query), self.command
        try:
            if path == "/v3/api-docs":
                return self.write_json(openapi())
            if path in ("/swagger-ui.html", "/swagger-ui"):
                return self.write_bytes(swagger_html().encode(), "text/html; charset=utf-8")
            conn = connect()
            try:
                data = self.dispatch(conn, method, path, query)
                if isinstance(data, tuple) and data[0] == "raw":
                    return self.write_bytes(data[1], data[2], data[3] if len(data) > 3 else None)
                return self.write_json(ok(data))
            finally:
                conn.close()
        except ApiError as e:
            self.write_json(error(e.code, e.message), 200 if e.code in (401, 404, 409, 422) else e.code)
        except Exception as e:
            self.write_json(error(500, f"系统异常: {e}"), 500)

    def dispatch(self, conn, method, path, query):
        body = self.json_body() if method in ("POST", "PUT") else {}
        if method == "POST" and path == "/api/auth/login": return svc.login(conn, body)
        if method == "POST" and path == "/api/auth/refresh":
            claims = parse_token(body.get("refreshToken", ""))
            if not claims or claims.get("type") != "refresh":
                raise ApiError(401, "refreshToken 已失效，请重新登录")
            username = claims.get("sub", "admin")
            return {"accessToken": svc.create_token(username, claims.get("user_id", 1), "access", 7200) if hasattr(svc, "create_token") else "", "refreshToken": body.get("refreshToken"), "userId": claims.get("user_id", 1), "username": username, "realName": None}
        if method == "POST" and path == "/api/auth/logout": return None
        if method == "GET" and path == "/api/auth/me": return svc.get_user(conn, self.current_user_id())
        if method == "GET" and path in ("/api/system/menus/tree", "/api/system/menus/my-tree"): return svc.menu_tree(conn)
        if method == "POST" and path == "/api/system/menus": return svc.create_menu(conn, body)
        if m := re.fullmatch(r"/api/system/menus/(\d+)", path):
            if method == "PUT": return svc.update_menu(conn, int(m[1]), body)
            if method == "DELETE": return svc.delete_menu(conn, int(m[1]))
        if method == "GET" and path == "/api/system/users": return svc.list_users(conn, query)
        if method == "POST" and path == "/api/system/users": return svc.create_user(conn, body)
        if m := re.fullmatch(r"/api/system/users/(\d+)", path):
            if method == "GET": return svc.get_user(conn, int(m[1]))
            if method == "PUT": return svc.update_user(conn, int(m[1]), body)
            if method == "DELETE": return svc.delete_user(conn, int(m[1]))
        if m := re.fullmatch(r"/api/system/users/(\d+)/reset-password", path):
            if method == "PUT": return svc.update_user(conn, int(m[1]), {"password": body.get("newPassword")})
        if m := re.fullmatch(r"/api/system/users/(\d+)/status", path):
            if method == "PUT": return svc.update_user(conn, int(m[1]), {"status": body.get("status")})
        if method == "GET" and path == "/api/system/roles": return svc.list_roles(conn)
        if method == "POST" and path == "/api/system/roles": return svc.create_role(conn, body)
        if m := re.fullmatch(r"/api/system/roles/(\d+)", path):
            if method == "PUT": return svc.update_role(conn, int(m[1]), body)
            if method == "DELETE": return svc.delete_role(conn, int(m[1]))
        if m := re.fullmatch(r"/api/system/roles/(\d+)/menus", path):
            if method == "PUT": return svc.assign_role_menus(conn, int(m[1]), body)
        if method == "GET" and path == "/api/jobs": return svc.list_jobs(conn, query)
        if method == "POST" and path == "/api/jobs": return svc.save_job(conn, body)
        if m := re.fullmatch(r"/api/jobs/(\d+)", path):
            if method == "GET": return svc.get_job(conn, int(m[1]))
            if method == "PUT": return svc.save_job(conn, body, int(m[1]))
            if method == "DELETE": return svc.delete_job(conn, int(m[1]))
        if m := re.fullmatch(r"/api/jobs/(\d+)/(publish|close)", path):
            if method == "PUT": return svc.set_job_status(conn, int(m[1]), "OPEN" if m[2] == "publish" else "CLOSED")
        if method == "GET" and path == "/api/resumes": return svc.list_resumes(conn, query)
        if method == "POST" and path == "/api/resumes": return svc.save_resume(conn, body)
        if method == "POST" and path == "/api/resumes/upload": return self.handle_upload(conn)
        if method == "GET" and path == "/api/resumes/export": return self.export_resumes(conn)
        if m := re.fullmatch(r"/api/resumes/(\d+)", path):
            if method == "GET": return svc.get_resume(conn, int(m[1]))
            if method == "PUT": return svc.save_resume(conn, body, int(m[1]))
            if method == "DELETE": return svc.delete_resume(conn, int(m[1]))
        if m := re.fullmatch(r"/api/resumes/(\d+)/file", path):
            resume = svc.get_resume(conn, int(m[1]))
            fp = resume.get("filePath")
            if not fp: raise ApiError(404, "无附件")
            return ("raw", (UPLOAD_DIR / fp).read_bytes(), "application/octet-stream", fp)
        if method == "POST" and path == "/api/match/run": return svc.run_match(conn, body.get("positionId"))
        if m := re.fullmatch(r"/api/match/results/(\d+)", path):
            if method == "GET": return svc.match_results(conn, int(m[1]))
        if path == "/api/match/config":
            if method == "GET": return svc.get_match_config(conn)
            if method == "PUT": return svc.update_match_config(conn, body)
        if method == "GET" and path == "/api/applications": return svc.list_applications(conn, query)
        if method == "POST" and path == "/api/applications": return svc.create_application(conn, body)
        if m := re.fullmatch(r"/api/applications/(\d+)", path):
            if method == "GET": return svc.get_application(conn, int(m[1]))
        if m := re.fullmatch(r"/api/applications/(\d+)/status", path):
            if method == "PUT": return svc.change_application_status(conn, int(m[1]), body.get("status"), body.get("remark"))
        if m := re.fullmatch(r"/api/applications/(\d+)/logs", path):
            if method == "GET": return svc.application_logs(conn, int(m[1]))
        if method == "GET" and path == "/api/interviews": return svc.list_interviews(conn, query)
        if method == "POST" and path == "/api/interviews": return svc.create_interview(conn, body)
        if m := re.fullmatch(r"/api/interviews/(\d+)", path):
            if method == "GET": return svc.get_interview(conn, int(m[1]))
            if method == "PUT": return svc.update_interview(conn, int(m[1]), body)
        if m := re.fullmatch(r"/api/interviews/(\d+)/result", path):
            if method == "PUT": return svc.update_interview_result(conn, int(m[1]), body)
        if method == "GET" and path == "/api/stats/dashboard": return svc.dashboard(conn)
        if method == "GET" and path == "/api/stats/source": return svc.source_stats(conn, query)
        raise ApiError(404, "接口不存在")

    def handle_upload(self, conn):
        content_type = self.headers.get("Content-Type", "")
        match = re.search(r"boundary=([^;]+)", content_type)
        if not match:
            raise ApiError(400, "上传请求格式错误")
        boundary = ("--" + match.group(1).strip('"')).encode()
        length = int(self.headers.get("Content-Length", "0"))
        raw = self.rfile.read(length)
        filename, data = "resume.pdf", None
        for part in raw.split(boundary):
            if b'name="file"' not in part:
                continue
            head, _, body = part.partition(b"\r\n\r\n")
            fname = re.search(rb'filename="([^"]+)"', head)
            if fname:
                filename = fname.group(1).decode("utf-8", "ignore") or filename
            data = body.strip().removesuffix(b"--").strip(b"\r\n")
            break
        if data is None:
            raise ApiError(400, "文件不能为空")
        return svc.upload_resume(conn, filename, data)

    def export_resumes(self, conn):
        output = io.StringIO()
        writer = csv.writer(output)
        writer.writerow(["ID", "姓名", "手机", "邮箱", "最高学历", "期望职位", "来源", "创建时间"])
        for r in svc.list_resumes(conn, {"page": ["1"], "size": ["10000"]})["rows"]:
            writer.writerow([r.get("id"), r.get("name"), r.get("phone"), r.get("email"), r.get("highestEdu"), r.get("desiredPosition"), r.get("source"), r.get("createdTime")])
        return ("raw", output.getvalue().encode("utf-8-sig"), "text/csv; charset=utf-8", "resumes.csv")

    def log_message(self, fmt, *args):
        return


def run(host="127.0.0.1", port=19915):
    httpd = ThreadingHTTPServer((host, port), Handler)
    print(f"Python backend listening on http://{host}:{port}")
    httpd.serve_forever()
