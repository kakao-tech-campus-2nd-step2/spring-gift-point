package gift.dto;

import gift.domain.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class MemberDTO {

    @Email(message = "유효한 이메일을 입력해주세요.")
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    private String password;

    public MemberDTO() {
    }

    private MemberDTO(MemberDTOBuilder builder) {
        this.email = builder.email;
        this.password = builder.password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public static class MemberDTOBuilder {
        private String email;
        private String password;

        public MemberDTOBuilder email(String email) {
            this.email = email;
            return this;
        }

        public MemberDTOBuilder password(String password) {
            this.password = password;
            return this;
        }

        public MemberDTO build() {
            return new MemberDTO(this);
        }
    }

    public Member toEntity() {
        return new Member.MemberBuilder()
            .email(this.email)
            .password(this.password)
            .build();
    }
}
