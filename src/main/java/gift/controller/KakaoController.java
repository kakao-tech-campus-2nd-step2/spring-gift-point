package gift.controller;

import gift.config.KakaoProperties;
import gift.service.KakaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/oauth")
public class KakaoController {

    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService;

    @Autowired
    public KakaoController(KakaoProperties kakaoProperties, KakaoService kakaoService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoService = kakaoService;
    }

    @GetMapping("/kakao")
    public ModelAndView redirectToKakaoLogin() {
        String url = kakaoProperties.authUrl() + "?response_type=code&client_id="
            + kakaoProperties.clientId() + "&redirect_uri=" + kakaoProperties.redirectUri();
        return new ModelAndView("redirect:" + url);
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<String> loginWithKakao(@RequestParam String code) {
        String token = kakaoService.getAccessToken(code);
        String kakaoUserId = kakaoService.getKakaoUserId(token);
        String jwtToken = kakaoService.registerKakaoMember(kakaoUserId, token);
        return new ResponseEntity<>(jwtToken, HttpStatus.OK);
    }
}