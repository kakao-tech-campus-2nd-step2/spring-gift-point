package gift.auth.domain;

public class KakaoToken {

    public KakaoToken() {
    }

    public static class kakaoToken {

        private Integer code = 200;
        private String msg;

        private String access_token;
        private String token_type;
        private String refresh_token;
        private Integer expires_in;
        private String scope;
        private Integer refresh_token_expires_in;

        public kakaoToken() {
        }

        public kakaoToken(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public kakaoToken(String access_token, String token_type, String refresh_token,
            Integer expires_in, String scope, Integer refresh_token_expires_in) {
            this.access_token = access_token;
            this.token_type = token_type;
            this.refresh_token = refresh_token;
            this.expires_in = expires_in;
            this.scope = scope;
            this.refresh_token_expires_in = refresh_token_expires_in;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public Integer getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(Integer expires_in) {
            this.expires_in = expires_in;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public Integer getRefresh_token_expires_in() {
            return refresh_token_expires_in;
        }

        public void setRefresh_token_expires_in(Integer refresh_token_expires_in) {
            this.refresh_token_expires_in = refresh_token_expires_in;
        }
    }

    public static class kakaoInfo {

        private Integer expiresInMillis;
        private Long id;
        private Integer expires_in;
        private Integer appId;

        public kakaoInfo() {
        }

        public kakaoInfo(Integer expiresInMillis, Long id, Integer expires_in, Integer appId) {
            this.expiresInMillis = expiresInMillis;
            this.id = id;
            this.expires_in = expires_in;
            this.appId = appId;
        }

        public Integer getExpiresInMillis() {
            return expiresInMillis;
        }

        public void setExpiresInMillis(Integer expiresInMillis) {
            this.expiresInMillis = expiresInMillis;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Integer getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(Integer expires_in) {
            this.expires_in = expires_in;
        }

        public Integer getAppId() {
            return appId;
        }

        public void setAppId(Integer appId) {
            this.appId = appId;
        }
    }
}
