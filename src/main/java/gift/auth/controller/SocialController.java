package gift.auth.controller;

import gift.auth.domain.KakaoToken.kakaoToken;
import gift.auth.service.SocialService;
import gift.util.page.SingleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "소셜 로그인 관련 서비스")
@RestController
@RequestMapping("/api/social")
public class SocialController {

    private final SocialService socialService;

    @Autowired
    public SocialController(SocialService socialService) {
        this.socialService = socialService;
    }

    @Operation(summary  = "카카오 코드로 카카오 토큰을 받아온다.", description = "카카오코드를 기반으로 토큰을 조회해서 반환합니다.")
    @GetMapping("/token/kakao")
    public SingleResult<kakaoToken> GetKakaoToken(@Valid @RequestParam String code) {
        return new SingleResult<>(socialService.getKakaoToken(code));
    }

    //    타인을 강제로 할 우려가 있어 토큰내 값으로 유저 사용
    //    로그인 상태에서 가능
    @Operation(summary  = "유저에 카카오 인증을 추가한다.", description = "카카오 토큰에 포함된 id를 유저에 매핑합니다.")
    @PostMapping("/kakao")
    public SingleResult<Long> SetToKakao(HttpServletRequest req, @RequestBody kakaoToken token) {
        return new SingleResult<>(socialService.SetToKakao(req, token));
    }
}
