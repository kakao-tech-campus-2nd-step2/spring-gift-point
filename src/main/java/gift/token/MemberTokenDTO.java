package gift.token;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "토큰 수신용 DTO", example = "Bearer header.payload.signature")
public class MemberTokenDTO {

    @Schema(description = "사용자 이메일")
    private String email;

    public MemberTokenDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
