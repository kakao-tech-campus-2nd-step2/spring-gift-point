package gift.dto;

public class KakaoDTO {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private Integer expires_in;
    private String scope;
    private Integer refresh_token_expires_in;

    public KakaoDTO(String access_token, String token_type, String refresh_token,
        Integer expires_in, String scope, Integer refresh_token_expires_in) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.refresh_token = refresh_token;
        this.expires_in = expires_in;
        this.scope = scope;
        this.refresh_token_expires_in = refresh_token_expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public String getScope() {
        return scope;
    }

    public Integer getRefresh_token_expires_in() {
        return refresh_token_expires_in;
    }

}
