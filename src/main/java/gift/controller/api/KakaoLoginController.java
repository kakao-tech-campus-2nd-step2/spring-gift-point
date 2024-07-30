package gift.controller.api;

import gift.dto.response.JwtResponse;
import gift.dto.response.KakaoTokenResponse;
import gift.service.KakaoApiService;
import gift.service.MemberService;
import gift.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class KakaoLoginController {

    private final MemberService memberService;
    private final TokenService tokenService;
    private final KakaoApiService kakaoApiService;
    @Value("authorizationCode.request.uri")
    private String authorizationCodeRequestUri;

    public KakaoLoginController(MemberService memberService, TokenService tokenService, KakaoApiService kakaoApiService) {
        this.memberService = memberService;
        this.tokenService = tokenService;
        this.kakaoApiService = kakaoApiService;
    }

    @GetMapping("/")
    public ResponseEntity<JwtResponse> getJwtToken(@RequestParam("code") String code) {
        KakaoTokenResponse kakaoToken = kakaoApiService.getKakaoToken(code);

        String email = kakaoApiService.getMemberEmail(kakaoToken.accessToken());
        Long memberId = memberService.findMemberIdByEmail(email);

        JwtResponse JwtResponse = tokenService.generateJwt(memberId);
        tokenService.saveKakaoAccessToken(memberId, kakaoToken);

        return ResponseEntity.ok().body(JwtResponse);
    }

    @GetMapping("/api/kakaologin")
    public ResponseEntity<Void> redirect() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(authorizationCodeRequestUri));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
