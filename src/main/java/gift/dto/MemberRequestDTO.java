package gift.dto;

import gift.model.Member;

public class MemberRequestDTO {
    private String email;
    private String password;
    private String token;

    public MemberRequestDTO(String email, String password, String token) {
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public MemberRequestDTO() {
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

    public void setToken(String token) {
        this.token = token;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Member toEntity() {
        return new Member(this.email, this.password);
    }
}
