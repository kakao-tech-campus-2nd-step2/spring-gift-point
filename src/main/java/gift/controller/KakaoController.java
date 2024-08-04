package gift.controller;


import gift.dto.KakaoMember;
import gift.dto.KakaoTokenResponseDto;
import gift.model.Member;
import gift.repository.MemberRepository;
import gift.service.KakaoService;
import gift.service.MemberService;
import gift.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class KakaoController {
    private final JwtUtil jwtUtil;
    private final KakaoService kakaoService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    public KakaoController(KakaoService kakaoService, MemberService memberService,
        MemberRepository memberRepository) {
        this.jwtUtil = new JwtUtil();
        this.kakaoService = kakaoService;
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/kakao")
    public void authorize(HttpServletResponse response) throws IOException {
        response.sendRedirect("https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=8b0993ea8425d3f401667223d8d6b1a7&redirect_uri=http://13.125.235.182:8080/api/auth/kakao/callback");

    }

    @GetMapping("/kakao/callback")
    @ResponseBody
    public ResponseEntity<?> token(@RequestParam("code") String code) {
        Map<String, Object> responseBody = new HashMap<>();
        KakaoTokenResponseDto kakaoTokenResponseDto = kakaoService.getAccessTokenFromKakao(code);
        KakaoMember kakaoMember = kakaoService.getKakaoProfile(kakaoTokenResponseDto);
        String token = memberService.loginKakaoMember(kakaoMember);
        Claims claims = jwtUtil.extractClaims(token.replace("Bearer ", ""));
        Long memberId = Long.parseLong(claims.getSubject());
        Member member = memberService.getMemberById(memberId);
        member.setAccessToken(token);// jwt 토큰 멤버 DB에 저장
        member.setKakaoToken(kakaoTokenResponseDto.getAccessToken());// 카카오에서 발급받은 엑세스 토큰도 멤버 DB에 저장
        memberService.updateMember(member);
        return ResponseEntity.status(HttpStatus.OK)
            .header(HttpHeaders.AUTHORIZATION, token)
            .build();
    }

}
