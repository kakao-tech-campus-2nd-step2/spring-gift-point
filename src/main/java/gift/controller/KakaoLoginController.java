package gift.controller;

import gift.auth.JwtTokenUtil;
import gift.model.Member;
import gift.service.KakaoAuthService;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Tag(name = "Kakao API", description = "APIs related to Kakao login operations")
public class KakaoLoginController {

    @Autowired
    private KakaoAuthService kakaoAuthService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/kakao/callback")
    @Operation(summary = "카카오 로그인 콜백", description = "카카오 로그인 후 리디렉션된 콜백을 처리")
    public String kakaoCallback(@RequestParam String code, Model model, HttpSession session) {
        String accessToken = kakaoAuthService.getAccessToken(code);
        session.setAttribute("accessToken", accessToken);

        String email = kakaoAuthService.getUserEmail(accessToken); // 사용자 이메일 가져오기
        Member member = memberService.findByEmail(email);
        if (member == null) {
            member = memberService.register(email, ""); // 비밀번호는 빈 문자열로 설정
        }

        String jwtToken = jwtTokenUtil.generateToken(member);
        session.setAttribute("jwtToken", jwtToken); // JWT 토큰 세션에 저장
        model.addAttribute("jwtToken", jwtToken);
        return "KakaoSuccess";
    }
}