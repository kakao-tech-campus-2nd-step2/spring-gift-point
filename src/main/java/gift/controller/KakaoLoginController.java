package gift.controller;


import gift.service.KakaoLoginService;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/kakao-login")
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService ;


    @Autowired
    public KakaoLoginController(KakaoLoginService kakaoLoginService){
        this.kakaoLoginService = kakaoLoginService ;
    }

    @GetMapping("")
    public String getAuthorizeCode() {
        String authorizationURI = kakaoLoginService.makeKakaoAuthorizationURI();
        return "redirect:" + authorizationURI;
    }


    @GetMapping("/callback")
    public ResponseEntity<String> getKakaoToken(@RequestParam(name = "code") String code) {
        String accessToken = kakaoLoginService.getKakaoAuthorizationToken(code);
        return ResponseEntity.status(HttpStatus.OK).body(accessToken);
    }

    @GetMapping("/{token}")
    public ResponseEntity<String> valdidateKakaoToken(@PathVariable("token") String token) {
        System.out.println(token);
        kakaoLoginService.validationKaKaoToken(token);
        return ResponseEntity.status(HttpStatus.OK)
                .body(token);
    }

    @GetMapping("/{token}/info")
    public ResponseEntity<String> getKakaoTokenInfo(@PathVariable("token") String token) {
        System.out.println(token);
        String email = kakaoLoginService.getKakaoEmailByToken(token);
        String jwtToken = kakaoLoginService.checkEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body("jwt token " + jwtToken);

    }




}
