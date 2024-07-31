package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.dto.KakaoInfoDto;
import gift.model.member.KakaoProperties;
import gift.model.member.Member;
import gift.service.KakaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("")
@Tag(name = "KakaoLogin", description = "KakaoLogin API")
public class KakaoLoginController {

    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService;

    public KakaoLoginController(KakaoProperties kakaoProperties, KakaoService kakaoService){
        this.kakaoProperties = kakaoProperties;
        this.kakaoService = kakaoService;
    }

    @GetMapping("/kakao/login")
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
        String accessToken = kakaoService.getAccessTokenFromKakao(code);

        KakaoInfoDto kakaoInfoDto = kakaoService.getUserInfo(accessToken);
        String email = kakaoInfoDto.getId() + "@naver.com";
        Member kakaoMember = kakaoService.registerOrGetKakaoMember(email);

        session.setAttribute("loginMember", kakaoMember);
        session.setMaxInactiveInterval(60 * 30);
        session.setAttribute("kakaoToken", accessToken);

        return ResponseEntity.ok("Access Token: " + accessToken);
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
