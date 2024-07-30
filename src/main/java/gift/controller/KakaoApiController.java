package gift.controller;

import gift.model.JwtTokenDTO;
import gift.model.OrderDTO;
import gift.service.KakaoApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

@RestController
public class KakaoApiController {

    @Value("${kakao.client_id}")
    private String clientId;
    @Value("${kakao.redirect_uri}")
    private String redirectUri;
    private final KakaoApiService kakaoApiService;

    public KakaoApiController(KakaoApiService kakaoApiService) {
        this.kakaoApiService = kakaoApiService;
    }

    @GetMapping("/oauth/kakao/login")
    public ResponseEntity<?> login() {
        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code"
            + "&client_id=" + clientId
            + "&redirect_uri=" + redirectUri;

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(kakaoAuthUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/api/kakao/callback") // callback은 굳이 건들지 말고
    @Operation(summary = "인가 코드를 통해 Access Token을 발급합니다.",
        description = "인가 코드로 Token 을 발급함과 동시에 카카오톡 프로필 닉네임과 토큰을 각각 멤버(Member)의 이메일과 비밀번호로 등록합니다."
            + " 그리고 해당 멤버의 id(memberId)와 Access Token 을 사용자에게 알려줍니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "정상적으로 주문 카카오톡 메시지가 전송됩니다.",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = OrderDTO.class))}),
        @ApiResponse(responseCode = "400", description = "입력값이 유효하지 않습니다.",
            content = @Content),
        @ApiResponse(responseCode = "500", description = "내부 서버 에러입니다.",
            content = @Content)}
    )
    public ResponseEntity<?> callback(
        @Parameter(description = "인가 코드", required = true)
        @RequestParam("code") String code) {
        JwtTokenDTO jwtTokenDTO = kakaoApiService.createKakaoMember(code);
        return ResponseEntity.status(HttpStatus.OK).body(jwtTokenDTO);
    }

    @ExceptionHandler(RestClientException.class)
    public String handleModelValidationExceptions(RestClientException ex, Model model) {
        String errorMessage = ex.getMessage();
        model.addAttribute("Error", errorMessage);
        return "error";
    }
}