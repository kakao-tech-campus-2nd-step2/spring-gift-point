package gift.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 요청 DTO")
public class MemberRequest {

    @Schema(description = "사용자 이메일", example = "user@example.com")
    String email;
    @Schema(description = "사용자 비밀번호", example = "password")
    String password;

    private MemberRequest() {
    }

    public MemberRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


}
