package gift.member.dto;

import gift.member.entity.Member;
import gift.token.MemberTokenDTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 정보 수신용 DTO", example = "{\"email\":\"aaa@email.com,\"password\":\"aaa\"}")
public class MemberRequestDTO {

    @Schema(description = "사용자 이메일")
    private String email;

    @Schema(description = "사용자 비밀번호")
    private String password;

    public MemberRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public Member toEntity() {
        return new Member(email, password);
    }

    public MemberTokenDTO toTokenDTO() {
        return new MemberTokenDTO(email);
    }
}
