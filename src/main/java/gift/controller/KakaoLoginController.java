package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.config.KakaoAuthProperties;
import gift.domain.KakaoInfo;
import gift.dto.MemberDto;
import gift.response.JwtResponse;
import gift.service.KakaoService;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@Tag(name = "카카오 OAuth", description = "카카오 OAuth API")
public class KakaoLoginController {
    private final KakaoService kakaoService;
    private final MemberService memberService;
    private final KakaoAuthProperties kakaoAuthProperties;
    private String baseUrl;

    public KakaoLoginController(KakaoService kakaoService, MemberService memberService,
        KakaoAuthProperties kakaoAuthProperties) {
        this.kakaoService = kakaoService;
        this.memberService = memberService;
        this.kakaoAuthProperties = kakaoAuthProperties;
    }

    @GetMapping("/api/members/kakao")
    @Operation(summary = "카카오 로그인 페이지 리다이렉트", description = "카카오 로그인 페이지로 리다이렉트한다.")
    public RedirectView redirectLoginPage(@RequestParam("redirect_url") String redirectUrl) {
        baseUrl = redirectUrl;
        System.out.println("baseUrl = " + baseUrl);

        String location = UriComponentsBuilder.fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
            .queryParam("response_type", "code")
            .queryParam("client_id", kakaoAuthProperties.getClientId())
            .queryParam("redirect_uri", kakaoAuthProperties.getRedirectUri())
            .toUriString();

        return new RedirectView(location);
    }

    @GetMapping("/kakao")
    @Operation(summary = "카카오 로그인", description = "카카오 계정으로 로그인한다.")
    public RedirectView loginByKakao(@RequestParam("code") String code) {
        // 1. 인가 코드 받기 (code)

        try {
            // 2. 인가 코드로 토큰 발급
            String accessToken = kakaoService.getKakaoToken(code);
            // 3. 토큰에서 사용자 정보 가져오기
            KakaoInfo kakaoInfo = kakaoService.getKakaoInfo(accessToken);

            if (memberService.findByEmail(kakaoInfo.getEmail()).isPresent()) {
                // 회원이 존재하면 로그인 처리 및 JWT 발급
                String jwt = memberService.login(new MemberDto(kakaoInfo.getEmail(), kakaoInfo.getPassword()));
                return new RedirectView(baseUrl + "?token=" + jwt);

            } else {
                // 4. 회원이 없으면 회원가입 후 로그인 처리 및 JWT 발급
                memberService.registerMember(new MemberDto(kakaoInfo.getEmail(), kakaoInfo.getPassword()), accessToken);
                String jwt = memberService.login(new MemberDto(kakaoInfo.getEmail(), kakaoInfo.getPassword()));
                return new RedirectView(baseUrl + "?token=" + jwt);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
