package gift.domain;

public class getTokenDto {
    private String grant_type;
    private String client_id;
    private String redirect_url;
    private String code;

    public getTokenDto(String grant_type, String client_id, String redirect_url, String code) {
        this.grant_type = grant_type;
        this.client_id = client_id;
        this.redirect_url = redirect_url;
        this.code = code;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getRedirect_url() {
        return redirect_url;
    }

    public String getCode() {
        return code;
    }
}
