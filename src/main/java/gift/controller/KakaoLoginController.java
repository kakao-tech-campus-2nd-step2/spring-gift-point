package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.dto.kakaoDto.KakaoInfoDto;
import gift.dto.memberDto.MemberDto;
import gift.model.member.KakaoProperties;
import gift.service.KakaoService;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/oauth/kakao")
@Tag(name = "KakaoLogin", description = "KakaoLogin API")
public class KakaoLoginController {

    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService;
    private final MemberService memberService;

    public KakaoLoginController(KakaoProperties kakaoProperties, KakaoService kakaoService,MemberService memberService){
        this.kakaoProperties = kakaoProperties;
        this.kakaoService = kakaoService;
        this.memberService = memberService;
    }

    @GetMapping
    @Operation(summary = "카카오를 통한 회원가입", description = "카카오를 통해 회원가입 할 때 사용하는 API")
    public String kakaoLogin() {
        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id="+kakaoProperties.getClientId());
        url.append("&redirect_uri="+kakaoProperties.getRedirectUri());
        url.append("&response_type=code");
        return "redirect:" + url.toString();
    }

    @GetMapping("/callback")
    @Operation(summary = "카카오 권한 동의 후 callback", description = "권한 동의 후 callback 할 때 사용하는 API")
    public ResponseEntity<String> callback(@RequestParam("code") String code, HttpSession session) throws JsonProcessingException {
        String kakaoAccessToken = kakaoService.getAccessTokenFromKakao(code);

        KakaoInfoDto kakaoInfoDto = kakaoService.getUserInfo(kakaoAccessToken);
        MemberDto kakaoMember = kakaoService.registerOrGetKakaoMember(kakaoInfoDto);
        String jwtToken = memberService.returnToken(kakaoMember);

        session.setAttribute("loginMember", kakaoMember);
        session.setMaxInactiveInterval(60 * 30);
        session.setAttribute("kakaoToken", kakaoAccessToken);
        session.setAttribute("jwtToken", jwtToken);

        return ResponseEntity.ok().body("Access Token: " + kakaoAccessToken);
    }

    @GetMapping("/kakao/logout")
    @Operation(summary = "카카오 로그아웃", description = "카카오를 통해 로그아웃 할 때 사용하는 API")
    public String kakaoLogout(HttpSession session) {
        String accessToken = (String) session.getAttribute("kakaoToken");

        if(accessToken != null && !"".equals(accessToken)) {
            kakaoService.kakaoDisconnect(accessToken);
            session.removeAttribute("kakaoToken");
            session.removeAttribute("loginMember");
        }
        return "redirect:/";
    }
}
