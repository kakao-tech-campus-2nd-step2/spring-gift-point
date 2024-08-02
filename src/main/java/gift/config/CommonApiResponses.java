package gift.config;

import static gift.util.constants.MemberConstants.INVALID_AUTHORIZATION_HEADER;
import static gift.util.constants.MemberConstants.INVALID_CREDENTIALS;
import static gift.util.constants.auth.TokenConstants.EXPIRED_TOKEN;
import static gift.util.constants.auth.TokenConstants.INVALID_TOKEN;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class CommonApiResponses {

    @ApiResponse(
        responseCode = "401",
        description = "유효하지 않은 Authorization 헤더 또는 토큰",
        content = @Content(
            mediaType = "application/json",
            examples = {
                @ExampleObject(
                    name = "유효하지 않은 Authorization 헤더",
                    value = "{\"error\": \"" + INVALID_AUTHORIZATION_HEADER + "\"}"
                ),
                @ExampleObject(name = "유효하지 않은 JWT 토큰", value = "{\"error\": \"" + INVALID_TOKEN + "\"}"),
                @ExampleObject(name = "만료된 JWT 토큰", value = "{\"error\": \"" + EXPIRED_TOKEN + "\"}")
            }
        )
    )
    @Retention(RetentionPolicy.RUNTIME)
    public @interface CommonUnauthorizedResponse {

    }

    @ApiResponse(
        responseCode = "403",
        description = "JWT 토큰으로 회원 찾기 실패",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(value = "{\"error\": \"" + INVALID_CREDENTIALS + "\"}")
        )
    )
    @Retention(RetentionPolicy.RUNTIME)
    public @interface CommonForbiddenResponse {

    }

    @ApiResponse(
        responseCode = "500",
        description = "서버 오류",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(value = "{\"error\": \"(서버 오류 메시지)\"}")
        )
    )
    @Retention(RetentionPolicy.RUNTIME)
    public @interface CommonServerErrorResponse {

    }
}
