package gift.product.controller;

import gift.product.dto.TokenDTO;
import gift.product.service.KakaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/kakao")
public class KakaoController {

    private final KakaoService kakaoService;

    public KakaoController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @GetMapping("/login")
    public String login() {
        return "redirect:" + kakaoService.getAuthCode();
    }

    @GetMapping("/callback")
    public ResponseEntity<TokenDTO> handleKakaoCallback(@RequestParam("code") String code) {
        return ResponseEntity.ok(kakaoService.login(code));
    }
}
