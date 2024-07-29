package gift.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@Schema(description = "Response DTO for Kakao User")
public record KakaoUserResponse(
    @Schema(description = "User ID", example = "123456789")
    Long id,
    @Schema(description = "Connection time", example = "2024-07-28T17:04:18.834374")
    String connectedAt,
    KakaoAccount kakaoAccount) {

    @Schema(description = "Kakao Account details")
    public record KakaoAccount(
        Profile profile,
        @Schema(description = "Is email valid", example = "true")
        boolean isEmailValid,
        @Schema(description = "Is email verified", example = "true")
        boolean isEmailVerified,
        @Schema(description = "User email", example = "user@example.com")
        String email) {

        @Schema(description = "User Profile")
        public record Profile(
            @Schema(description = "User nickname", example = "nickname_example")
            String nickname) {

        }
    }
}
