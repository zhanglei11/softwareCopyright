import base64
import hashlib
import hmac
import json
import time
from datetime import datetime
from typing import Any

JWT_SECRET = b"AnguResumeJobMatcher@SecretKey2026!"


class ApiError(Exception):
    def __init__(self, code: int, message: str):
        self.code = code
        self.message = message
        super().__init__(message)


def now() -> str:
    return datetime.now().strftime("%Y-%m-%d %H:%M:%S")


def ok(data: Any = None) -> dict[str, Any]:
    return {"code": 200, "message": "操作成功", "data": data}


def error(code: int, message: str) -> dict[str, Any]:
    return {"code": code, "message": message, "data": None}


def table(rows: list[dict[str, Any]], total: int) -> dict[str, Any]:
    return {"rows": rows, "total": total}


def page_params(query: dict[str, list[str]]) -> tuple[int, int]:
    page = max(int(first(query, "page", "1") or 1), 1)
    size = min(max(int(first(query, "size", "20") or 20), 1), 200)
    return size, (page - 1) * size


def first(query: dict[str, list[str]], key: str, default: str | None = None) -> str | None:
    values = query.get(key)
    return values[0] if values else default


def b64url(data: bytes) -> str:
    return base64.urlsafe_b64encode(data).rstrip(b"=").decode()


def b64url_decode(data: str) -> bytes:
    padding = "=" * (-len(data) % 4)
    return base64.urlsafe_b64decode((data + padding).encode())


def create_token(username: str, user_id: int, token_type: str, ttl_seconds: int) -> str:
    header = {"typ": "JWT", "alg": "HS256"}
    payload = {"sub": username, "user_id": user_id, "type": token_type, "exp": int(time.time()) + ttl_seconds}
    signing_input = f"{b64url(json.dumps(header, separators=(',', ':')).encode())}.{b64url(json.dumps(payload, separators=(',', ':')).encode())}"
    sig = hmac.new(JWT_SECRET, signing_input.encode(), hashlib.sha256).digest()
    return f"{signing_input}.{b64url(sig)}"


def parse_token(token: str) -> dict[str, Any] | None:
    try:
        head, payload, sig = token.split(".")
        expected = b64url(hmac.new(JWT_SECRET, f"{head}.{payload}".encode(), hashlib.sha256).digest())
        if not hmac.compare_digest(sig, expected):
            return None
        claims = json.loads(b64url_decode(payload))
        if int(claims.get("exp", 0)) < int(time.time()):
            return None
        return claims
    except Exception:
        return None


def camelize_row(row: dict[str, Any]) -> dict[str, Any]:
    mapping = {
        "real_name": "realName",
        "error_count": "errorCount",
        "locked_until": "lockedUntil",
        "created_time": "createdTime",
        "updated_time": "updatedTime",
        "parent_id": "parentId",
        "menu_type": "menuType",
        "menu_name": "menuName",
        "perm_code": "permCode",
        "role_name": "roleName",
        "role_code": "roleCode",
        "job_type": "jobType",
        "salary_min": "salaryMin",
        "salary_max": "salaryMax",
        "edu_require": "eduRequire",
        "exp_require": "expRequire",
        "skill_tags": "skillTags",
        "creator_id": "creatorId",
        "birth_date": "birthDate",
        "desired_position": "desiredPosition",
        "desired_city": "desiredCity",
        "desired_salary_min": "desiredSalaryMin",
        "desired_salary_max": "desiredSalaryMax",
        "job_status": "jobStatus",
        "highest_edu": "highestEdu",
        "total_exp_years": "totalExpYears",
        "file_path": "filePath",
        "parse_success": "parseSuccess",
        "resume_id": "resumeId",
        "skill_name": "skillName",
        "edu_level": "eduLevel",
        "start_date": "startDate",
        "end_date": "endDate",
        "position_id": "positionId",
        "total_score": "totalScore",
        "skill_score": "skillScore",
        "edu_score": "eduScore",
        "exp_score": "expScore",
        "skill_weight": "skillWeight",
        "edu_weight": "eduWeight",
        "exp_weight": "expWeight",
        "updater_id": "updaterId",
        "matched_at": "matchedAt",
        "operate_time": "operateTime",
        "operator_id": "operatorId",
        "position_title": "positionTitle",
        "resume_name": "resumeName",
        "application_id": "applicationId",
        "from_status": "fromStatus",
        "to_status": "toStatus",
        "operator_name": "operatorName",
        "interview_time": "interviewTime",
    }
    return {mapping.get(k, k): v for k, v in row.items()}
