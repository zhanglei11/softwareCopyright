package api

import (
	"crypto/hmac"
	"crypto/sha256"
	"encoding/base64"
	"encoding/json"
	"net/http"
	"strconv"
	"strings"
	"time"
)

var jwtSecret = []byte("AnguResumeJobMatcher@SecretKey2026!")

type AjaxResult struct {
	Code    int         `json:"code" description:"业务状态码，成功为 200"`
	Message string      `json:"message" description:"响应消息"`
	Data    interface{} `json:"data" description:"业务数据，无数据时为 null"`
}

type APIError struct {
	Code    int
	Message string
}

func (e APIError) Error() string { return e.Message }

func Ok(data interface{}) AjaxResult {
	return AjaxResult{Code: 200, Message: "操作成功", Data: data}
}

func Error(code int, msg string) AjaxResult {
	return AjaxResult{Code: code, Message: msg, Data: nil}
}

func WriteJSON(w http.ResponseWriter, v interface{}) {
	w.Header().Set("Content-Type", "application/json; charset=utf-8")
	_ = json.NewEncoder(w).Encode(v)
}

func ReadJSON(r *http.Request) (map[string]interface{}, error) {
	var body map[string]interface{}
	if r.Body == nil {
		return map[string]interface{}{}, nil
	}
	err := json.NewDecoder(r.Body).Decode(&body)
	if err != nil {
		return map[string]interface{}{}, nil
	}
	return body, nil
}

func Str(body map[string]interface{}, key string) string {
	if v, ok := body[key].(string); ok {
		return v
	}
	return ""
}

func Int(body map[string]interface{}, key string) int64 {
	switch v := body[key].(type) {
	case float64:
		return int64(v)
	case int:
		return int64(v)
	case int64:
		return v
	case string:
		n, _ := strconv.ParseInt(v, 10, 64)
		return n
	default:
		return 0
	}
}

func Strings(body map[string]interface{}, key string) []string {
	items, ok := body[key].([]interface{})
	if !ok {
		return nil
	}
	out := make([]string, 0, len(items))
	for _, item := range items {
		if s, ok := item.(string); ok {
			out = append(out, s)
		}
	}
	return out
}

func IDs(body map[string]interface{}, key string) []int64 {
	items, ok := body[key].([]interface{})
	if !ok {
		return nil
	}
	out := make([]int64, 0, len(items))
	for _, item := range items {
		switch v := item.(type) {
		case float64:
			out = append(out, int64(v))
		case int64:
			out = append(out, v)
		}
	}
	return out
}

func Page(r *http.Request) (limit int, offset int) {
	page, _ := strconv.Atoi(r.URL.Query().Get("page"))
	size, _ := strconv.Atoi(r.URL.Query().Get("size"))
	if page < 1 {
		page = 1
	}
	if size < 1 {
		size = 20
	}
	if size > 200 {
		size = 200
	}
	return size, (page - 1) * size
}

func Now() string {
	return time.Now().Format("2006-01-02 15:04:05")
}

func Token(username string, userID int64, typ string, ttl time.Duration) string {
	header := b64(`{"typ":"JWT","alg":"HS256"}`)
	payloadBytes, _ := json.Marshal(map[string]interface{}{
		"sub": username, "user_id": userID, "type": typ, "exp": time.Now().Add(ttl).Unix(),
	})
	payload := b64(string(payloadBytes))
	unsigned := header + "." + payload
	mac := hmac.New(sha256.New, jwtSecret)
	mac.Write([]byte(unsigned))
	return unsigned + "." + base64.RawURLEncoding.EncodeToString(mac.Sum(nil))
}

func ParseToken(raw string) map[string]interface{} {
	raw = strings.TrimPrefix(raw, "Bearer ")
	parts := strings.Split(raw, ".")
	if len(parts) != 3 {
		return nil
	}
	unsigned := parts[0] + "." + parts[1]
	mac := hmac.New(sha256.New, jwtSecret)
	mac.Write([]byte(unsigned))
	if !hmac.Equal([]byte(parts[2]), []byte(base64.RawURLEncoding.EncodeToString(mac.Sum(nil)))) {
		return nil
	}
	data, err := base64.RawURLEncoding.DecodeString(parts[1])
	if err != nil {
		return nil
	}
	var claims map[string]interface{}
	if json.Unmarshal(data, &claims) != nil {
		return nil
	}
	if exp, ok := claims["exp"].(float64); ok && int64(exp) < time.Now().Unix() {
		return nil
	}
	return claims
}

func b64(s string) string {
	return base64.RawURLEncoding.EncodeToString([]byte(s))
}
