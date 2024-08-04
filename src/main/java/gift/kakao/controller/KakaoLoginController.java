package gift.kakao.controller;

import gift.member.exception.NoSuchMemberException;
import gift.kakao.service.KakaoLoginService;
import gift.kakao.KakaoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/kakao")
public class KakaoLoginController {

    private final KakaoProperties kakaoProperties;
    private final KakaoLoginService kakaoLoginService;

    @Autowired
    public KakaoLoginController(KakaoProperties kakaoProperties, KakaoLoginService kakaoLoginService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoLoginService = kakaoLoginService;
    }

    @GetMapping("/login")
    public String kakaoLogin(Model model) {
        model.addAttribute("kakaoClientId", kakaoProperties.clientId());
        model.addAttribute("kakaoRedirectUri", kakaoProperties.redirectUri());
        return "login";
    }

    @RequestMapping("/oauth")
    public Object KakaoLogin(@RequestParam("code") String code) {
        try {
            return ResponseEntity.ok().body(kakaoLoginService.login(code));
        } catch (NoSuchMemberException e) {
            return "register";
        }
    }
}
