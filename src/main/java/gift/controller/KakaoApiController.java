package gift.controller;

import gift.model.MemberDTO;
import gift.service.KakaoApiService;
import gift.service.MemberService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kakao")
public class KakaoApiController {

    private final KakaoApiService kakaoApiService;
    private final MemberService memberService;

    public KakaoApiController(KakaoApiService kakaoApiService, MemberService memberService) {
        this.kakaoApiService = kakaoApiService;
        this.memberService = memberService;
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        String accessToken = kakaoApiService.getAccessToken(code);
        String nickname = kakaoApiService.getKakaoProfileNickname(accessToken);
        MemberDTO createdMember = memberService.createMember(
            new MemberDTO(null, nickname, accessToken));
        Map<String, String> response = new HashMap<>();
        response.put("Aceess Token: ", accessToken);
        response.put("Member ID: ", createdMember.id().toString());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}