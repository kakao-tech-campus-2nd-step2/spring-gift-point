package gift.web.dto.request.member;

import gift.domain.Member;
import gift.web.validation.constraints.Password;
import jakarta.validation.constraints.Email;

public class CreateMemberRequest {

    @Email
    private String email;

    @Password
    private String password;

    private String name;

    public CreateMemberRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public Member toEntity() {
        return new Member.Builder()
            .email(gift.domain.vo.Email.from(this.email))
            .password(gift.domain.vo.Password.from(password))
            .name(this.name)
            .build();
    }

}
