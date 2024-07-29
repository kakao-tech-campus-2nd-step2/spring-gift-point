package gift.controller;

import gift.config.KakaoProperties;
import gift.model.BearerToken;
import gift.service.KakaoAuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class KakaoController {
    private final KakaoAuthService kakaoAuthService;
    private final KakaoProperties kakaoProperties;

    public KakaoController(KakaoAuthService kakaoAuthService, KakaoProperties kakaoProperties) {
        this.kakaoAuthService = kakaoAuthService;
        this.kakaoProperties = kakaoProperties;
    }

    @GetMapping(value="/kakao")
    public String kakaoConnect() {
        System.out.println("redirect_uri=" + kakaoProperties.getRedirectUrl());
        System.out.println("client_id=" + kakaoProperties.getClientId());
        String url = UriComponentsBuilder.fromHttpUrl(kakaoProperties.getLoginUrl())
                .queryParam("redirect_uri", kakaoProperties.getRedirectUrl())
                .queryParam("client_id", kakaoProperties.getClientId())
                .build()
                .toUriString();
        return "redirect:" + url;
    }

    @GetMapping("/kakaoAuth")
    public String getKakaoAuthToken(@RequestParam String code){
        String token = kakaoAuthService.getKakaoToken(code);
        System.out.println("kakoAuth return : " + token);
        return "redirect:/products";
    }

    @GetMapping("/KakaoOrder/{productId}/{optionName}/{num}")
    @ResponseBody
    public void orderProduct(HttpServletRequest request, @PathVariable Long productId,
                             @PathVariable String optionName, @PathVariable int num){
        BearerToken token = (BearerToken) request.getAttribute("bearerToken");
        kakaoAuthService.orderProduct(token.getToken(), productId, optionName, num);
    }
}
