package gift.controller;

import gift.auth.Token;
import gift.service.KakaoApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth/kakao")
@Tag(name = "카카오 API", description = "카카오 OAuth 2.0을 통한 로그인에 관련된 API를 제공합니다.")
public class KakaoApiController {

    private final KakaoApiService kakaoApiService;

    public KakaoApiController(KakaoApiService kakaoApiService) {
        this.kakaoApiService = kakaoApiService;
    }

    /**
     * 인가 코드를 String으로 받고 이 코드로 카카오서버에 인가토큰(access_token)요청
     * @param code 인가 코드
     */
    @GetMapping("/callback")
    @Operation(
            summary = "카카오 로그인 콜백",
            description = "인가 코드를 받아 카카오 서버로 부터 인가 토큰(access token)을 요청하여 받아오는 API입니다."
    )
    @Parameter(name = "code", description = "카카오 서버로부터 받은 인가 코드", required = true)
    public ResponseEntity<Token> kakaoLogin(@RequestParam(value = "code") String code) {
        Token accessToken = kakaoApiService.getAccessToken(code);
        kakaoApiService.kakaoLogin(accessToken);
        return ResponseEntity.ok().body(accessToken);
    }

}
