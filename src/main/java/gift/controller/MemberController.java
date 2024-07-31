package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.domain.Member;
import gift.domain.MemberRequest;
import gift.service.KakaoService;
import gift.service.MemberService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;
    private final KakaoService kakaoService;

    public MemberController(MemberService memberService,KakaoService kakaoService) {
        this.memberService = memberService;
        this.kakaoService = kakaoService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> join(
            @RequestBody MemberRequest memberRequest
    ) {
        memberService.join(memberRequest);
        return ResponseEntity.ok().body("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody MemberRequest memberRequest
    ) {
        String jwt = memberService.login(memberRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwt);
        return ResponseEntity.ok().headers(headers).body("로그인 성공");
    }

    @GetMapping("/login/kakao")
    public RedirectView getUserAgree(){
        URI uri = kakaoService.makeUri();
        return new RedirectView(uri.toString());
    }

    @GetMapping("/login/kakao/callback")
    public ResponseEntity<String> getUserInfomation(
            @RequestParam("code") String code
    ) throws JsonProcessingException {
        String jwt =  kakaoService.getToken(code);
        return ResponseEntity.ok().body(jwt);
    }


    @PostMapping("/changePassword")
    public ResponseEntity changePassword(
            @RequestBody MemberRequest memberRequest
    ) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("changePassword is not allowed");
    }

    @PostMapping("/findPassword")
    public ResponseEntity findPassword(
            @RequestBody MemberRequest memberRequest
    ) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("findPassword is not allowed");
    }
}
