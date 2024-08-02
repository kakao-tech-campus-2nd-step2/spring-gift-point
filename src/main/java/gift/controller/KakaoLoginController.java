package gift.controller;


import gift.dto.response.LoginResponseDTO;
import gift.service.KakaoLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/members/kakao-login")
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
    public ResponseEntity<LoginResponseDTO> kakaoLogin(@RequestParam(name = "code") String code) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(kakaoLoginService.getKakaoEmailByToken(code));
    }

    /*@GetMapping("/{token}/info")
    public ResponseEntity<String> getKakaoTokenInfo(@PathVariable("token") String token) {
        String email = kakaoLoginService.getKakaoEmailByToken(token);
        String jwtToken = kakaoLoginService.checkEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body("jwt token " + jwtToken);
    }*/

}

