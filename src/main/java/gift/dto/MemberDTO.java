package gift.dto;

import gift.entity.Member;
import jakarta.validation.constraints.Email;


public class MemberDTO {

    @Email
    private String email;
    private String password;

    public MemberDTO() {
    }

    public MemberDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Member toEntity() {
        return new Member(email, password);
    }
}
