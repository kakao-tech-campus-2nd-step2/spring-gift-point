package gift.controller;

import gift.domain.KakaoProperties;
import gift.domain.KakaoTokenResponsed;
import gift.service.KakaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "KakaoLogin API")
@RestController
@RequestMapping("/kakao")
public class KakaoController {

    private final KakaoService kakaoService;
    private final KakaoProperties kakaoProperties;

    public KakaoController(KakaoService kakaoService, KakaoProperties kakaoProperties) {
        this.kakaoService = kakaoService;
        this.kakaoProperties = kakaoProperties;
    }

    @Operation(summary = "카카오 로그인 요청", description = "인가코드를 받은후 리다이렉션")
    @GetMapping("/login")
    public void redirectKaKaoLogin(HttpServletResponse response) throws IOException {
        String url = kakaoProperties.authUrl() +
            "?scope=talk_message&response_type=code&client_id=" + kakaoProperties.clientId() +
            "&redirect_uri=" + kakaoProperties.redirectUrl();
        response.sendRedirect(url);
    }

//    @GetMapping("/token")
//    public ResponseEntity<String> getAccessToken(@RequestParam String code){
//        String token = kakaoService.getAccessToken(code);
//        return new ResponseEntity<>(token, HttpStatus.OK);
//    }

    @Operation(summary = "카카오 로그인 및 토큰 발급", description = "jwt토큰을 반환받습니다.")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Jwt토큰 발급 성공",
                content = @Content(schema = @Schema(implementation = String.class)))
        })
    @GetMapping("/token")
    public ResponseEntity<String> getAccessToken(@RequestParam String code){
        KakaoTokenResponsed token = kakaoService.getTokeResponse(code);
        String jwt = kakaoService.LoginWithKakao(token.accessToken());
        return new ResponseEntity<>(jwt, HttpStatus.OK);
    }



}
