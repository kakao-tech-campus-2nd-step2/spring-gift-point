package gift.domain.Member.dto;

import gift.domain.Member.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class MemberDTO {

    @Email
    private String email;
    @NotBlank
    private String password;

    public MemberDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public MemberDTO() {
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Member toMember() {
        return new Member(this.email, this.password); // dto to entity
    }

    @Override
    public String toString() {
        return "MemberDTO{" +
               "email='" + email + '\'' +
               ", password='" + password + '\'' +
               '}';
    }
}
