package gift.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "로그인/회원가입 요청 DTO")
public record RequestMemberDTO(
        @NotBlank
        @Pattern(
                regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                message = "이메일 주소 양식을 확인해주세요"
        )
        @Schema(description = "이메일")
        String email,

        @NotBlank
        @Schema(description = "패스워드")
        String password
) {}


