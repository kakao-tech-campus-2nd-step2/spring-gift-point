package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.DTO.Token;
import gift.service.KakaoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;


@RestController
public class KakaoLoginController {
    private final KakaoService kakaoService;
    private String url;

    public KakaoLoginController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    /*
     * 카카오 로그인 페이지로 연결
     */
    @GetMapping("/kakaoLogin")
    public RedirectView OauthLogin(@RequestParam String code) throws JsonProcessingException {
        Token token = kakaoService.getKakaoToken(code);
        System.out.println("url = " + url);
        return new RedirectView(url + "?" + "token=" + token.getToken());
    }
    /*
     * 카카오 로그인
     */
    @PostMapping("api/members/kakao")
    public RedirectView getCode(HttpServletRequest request){
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        url = scheme + "://" + serverName + ":" + serverPort + contextPath;

        return new RedirectView(kakaoService.makeLoginUrl());
    }
}
