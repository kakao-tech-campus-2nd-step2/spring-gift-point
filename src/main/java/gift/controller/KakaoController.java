package gift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.KakaoProperties;
import gift.entity.TokenResponseEntity;
import gift.service.KakaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api/kakao")
@Tag(name = "Kakao")
public class KakaoController {

    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public KakaoController(KakaoProperties kakaoProperties, KakaoService kakaoService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoService = kakaoService;
    }

    @Operation(summary = "카카오 로그인 리디렉트", description = "카카오 로그인 페이지로 리디렉트합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "카카오 로그인 페이지로 리디렉트 성공")
    })
    @GetMapping("/login")
    public ModelAndView redirectToKakaoLogin() {
        String url = kakaoProperties.authUrl() + "?response_type=code&client_id="
            + kakaoProperties.clientId() + "&redirect_uri=" + kakaoProperties.redirectUri();
        return new ModelAndView("redirect:" + url);
    }

    @Operation(summary = "카카오 로그인 콜백", description = "카카오 로그인 콜백을 처리하여 JWT 토큰을 반환합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카카오 로그인 성공", content = @Content(schema = @Schema(implementation = TokenResponseEntity.class)))
    })
    @GetMapping("/login/callback")
    public ResponseEntity<String> loginWithKakao(@RequestParam String code) throws Exception {
        String token = kakaoService.getAccessToken(code);
        String kakaoUserId = kakaoService.getKakaoUserId(token);
        String jwtToken = kakaoService.registerKakaoMember(kakaoUserId, token);
        return new ResponseEntity<>(
            objectMapper.writeValueAsString(new TokenResponseEntity(jwtToken)), HttpStatus.OK);
    }
}