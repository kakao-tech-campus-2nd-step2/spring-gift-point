package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.DTO.Token;
import gift.service.KakaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;


@Controller
public class KakaoLoginController {
    private final KakaoService kakaoService;

    public KakaoLoginController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }
    /*
     * 카카오 로그인 이후 코드를 이용해 토큰 발급 및 메인 페이지로 전달
     */
    @GetMapping("/")
    public RedirectView kakaoLogin(
            @RequestParam(required = false) String code, RedirectAttributes redirectAttributes
    ) throws JsonProcessingException {
        if(code != null){
            Token login = kakaoService.getKakaoToken(code);
            redirectAttributes.addFlashAttribute("token", login.getToken());
            return new RedirectView("/home");
        }
        return new RedirectView("/home");
    }
    /*
     * 카카오 로그인 페이지로 연결
     */
    @GetMapping("/kakaoLogin")
    public String OauthLogin(){
        String url = kakaoService.makeLoginUrl();
        return "redirect:" + url;
    }
    /*
     * 카카오 로그인
     */
    @PostMapping("api/members/kakao")
    @ResponseBody
    public ResponseEntity<String> kakaoLogin(){
        return kakaoService.getCode();
    }
}
