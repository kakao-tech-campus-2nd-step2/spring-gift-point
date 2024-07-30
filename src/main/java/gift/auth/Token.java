package gift.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Token")
public record Token(

        @Schema(description = "Access Token")
        String token
) {
}
