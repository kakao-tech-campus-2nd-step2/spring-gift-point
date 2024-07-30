package gift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.KakaoProperties;
import gift.entity.TokenResponseEntity;
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
@RequestMapping("/kakao")
public class KakaoController {

    private final KakaoProperties kakaoProperties;
    private final KakaoService kakaoService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public KakaoController(KakaoProperties kakaoProperties, KakaoService kakaoService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoService = kakaoService;
    }

    @GetMapping("/login")
    public ModelAndView redirectToKakaoLogin() {
        String url = kakaoProperties.authUrl() + "?response_type=code&client_id="
            + kakaoProperties.clientId() + "&redirect_uri=" + kakaoProperties.redirectUri();
        return new ModelAndView("redirect:" + url);
    }

    @GetMapping("/login/callback")
    public ResponseEntity<String> loginWithKakao(@RequestParam String code) throws Exception {
        String token = kakaoService.getAccessToken(code);
        String kakaoUserId = kakaoService.getKakaoUserId(token);
        String jwtToken = kakaoService.registerKakaoMember(kakaoUserId, token);
        return new ResponseEntity<>(
            objectMapper.writeValueAsString(new TokenResponseEntity(jwtToken)), HttpStatus.OK);
    }
}