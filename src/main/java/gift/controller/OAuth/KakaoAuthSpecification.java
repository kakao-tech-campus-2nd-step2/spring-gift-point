package gift.controller.OAuth;

import gift.dto.OAuth.LoginInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;

@Tag(name = "카카오 인증", description = "카카오 인증을 위한 API")
public interface KakaoAuthSpecification {

    @Operation(summary = "카카오 로그인 페이지로 리다이렉트", description = "사용자를 카카오 로그인 페이지로 리다이렉트합니다.",
            responses = {
                    @ApiResponse(responseCode = "302", description = "리다이렉트 성공"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            })
    ResponseEntity<Void> getAuthCode(HttpServletResponse response, @RequestParam("redirect-url") String redirectUrl) throws IOException;

    @Operation(summary = "카카오 인증 코드로 액세스 토큰 가져오기", description = "카카오 인증 코드를 사용하여 액세스 토큰을 가져옵니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "액세스 토큰 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class),
                                    examples = @ExampleObject(value = "{\"access_token\": \"<token>\"}")
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패")
            })
    ResponseEntity<Map<String, String>> getAccessToken(@Parameter(description = "카카오 인증 코드") @RequestParam String code,@RequestParam("redirect-url") String redirectUrl);
}
