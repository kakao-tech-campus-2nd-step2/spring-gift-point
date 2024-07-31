package gift.controller.kakao;

import gift.DTO.kakao.KakaoLoginResponse;
import gift.DTO.kakao.KakaoMemberInfo;
import gift.DTO.kakao.KakaoSignupRequest;
import gift.domain.Member;
import gift.service.KakaoService;
import gift.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class KakaoController {

    private final KakaoService kakaoService;
    private final MemberService memberService;

    public KakaoController(KakaoService kakaoService, MemberService memberService) {
        this.kakaoService = kakaoService;
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<KakaoLoginResponse> kakaoLogin(
        @RequestParam("code") String authCode
    ) {
        String accessToken = kakaoService.sendTokenRequest(authCode);
        KakaoMemberInfo kakaoMemberInfo = kakaoService.getMemberInfo(accessToken);
        Long kakaoId = kakaoMemberInfo.id();
        if (memberService.getMemberByKakaoId(kakaoId) == null) { // sign-up
            memberService.registerKakaoMember(new KakaoSignupRequest(kakaoId));
        }
        return ResponseEntity.status(HttpStatus.OK).body(
            new KakaoLoginResponse(kakaoMemberInfo.id(), accessToken)
        );
    }

}
