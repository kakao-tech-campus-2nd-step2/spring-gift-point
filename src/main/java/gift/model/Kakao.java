package gift.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "kako_login")
public class Kakao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String grant_type = "authorization_code";

    @Column(unique = true, nullable = false)
    private String client_id = "8b0993ea8425d3f401667223d8d6b1a7";

    @Column(unique = true, nullable = false)
    private String redirect_uri = "http://13.125.235.182:8080";

    @Column(unique = true, nullable = false)
    private String code;

    public Kakao(){

    }

    public Kakao(String grant_type, String client_id, String redirect_uri, String code){
        this.grant_type = grant_type;
        this.client_id = client_id;
        this.redirect_uri = redirect_uri;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public void setRedirect_uri(String redirect_uri) {
        this.redirect_uri = redirect_uri;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
