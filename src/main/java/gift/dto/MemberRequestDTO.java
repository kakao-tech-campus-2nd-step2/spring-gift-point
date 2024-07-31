package gift.dto;

import gift.model.Member;

public class MemberRequestDTO {
    private String email;
    private String password;
    private String code; // 카카오 인증 코드 추가
    private String token;


    public MemberRequestDTO(String email, String password, String code) {
        this.email = email;
        this.password = password;
        this.code = code;
    }

    public MemberRequestDTO(String email, String password, String code, String token) {
        this.email = email;
        this.password = password;
        this.code = code;
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
