package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MemberDto {

    private Long id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("token")
    private String token;

    public MemberDto() {
    }

    public MemberDto(Long id, String email, String password, String token) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public MemberDto(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }
}