package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.domain.KakaoInfo;
import gift.dto.MemberDto;
import gift.response.JwtResponse;
import gift.service.KakaoService;
import gift.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoLoginController {
    private final KakaoService kakaoService;
    private final MemberService memberService;

    public KakaoLoginController(KakaoService kakaoService, MemberService memberService) {
        this.kakaoService = kakaoService;
        this.memberService = memberService;
    }

    @GetMapping("/kakao")
    public ResponseEntity<JwtResponse> kakaoLogin(@RequestParam("code") String code) {
        // 1. 인가 코드 받기 (code)

        try {
            // 2. 인가 코드로 토큰 발급
            String accessToken = kakaoService.getKakaoToken(code);
            // 3. 토큰에서 사용자 정보 가져오기
            KakaoInfo kakaoInfo = kakaoService.getKakaoInfo(accessToken);

            if (memberService.findByEmail(kakaoInfo.getEmail()).isPresent()) {
                // 회원이 존재하면 로그인 처리 및 JWT 발급
                String jwt = memberService.login(new MemberDto(kakaoInfo.getId(), kakaoInfo.getEmail(), kakaoInfo.getPassword(), accessToken));
                return ResponseEntity.ok().body(new JwtResponse(jwt));

            } else {
                // 4. 회원이 없으면 회원가입 후 로그인 처리 및 JWT 발급
                memberService.registerMember(new MemberDto(kakaoInfo.getId(), kakaoInfo.getEmail(), kakaoInfo.getPassword(), accessToken));
                String jwt = memberService.login(new MemberDto(kakaoInfo.getId(), kakaoInfo.getEmail(), kakaoInfo.getPassword(), accessToken));
                return ResponseEntity.status(HttpStatus.CREATED).body(new JwtResponse(jwt));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
