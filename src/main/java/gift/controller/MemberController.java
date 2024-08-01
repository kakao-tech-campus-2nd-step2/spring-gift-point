package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.domain.Auth.LoginRequest;
import gift.domain.Auth.TokenResponse;
import gift.domain.Member.MemberRequest;
import gift.service.KakaoService;
import gift.service.MemberService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.naming.AuthenticationException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

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
    public ResponseEntity<TokenResponse> join(
            @RequestBody MemberRequest memberRequest
    ) throws BadRequestException {
        memberService.join(memberRequest);
        String jwt = memberService.login(new LoginRequest(memberRequest.id(),memberRequest.password()));
        return ResponseEntity.ok().body(new TokenResponse(jwt));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @RequestBody LoginRequest loginRequest
            ) {
        String jwt = memberService.login(loginRequest);
        return ResponseEntity.ok().body(new TokenResponse(jwt));
    }

    @GetMapping("/login/kakao")
    public RedirectView getUserAgree(){
        URI uri = kakaoService.makeUri();
        return new RedirectView(uri.toString());
    }

    @GetMapping("/login/kakao/callback")
    public ResponseEntity<TokenResponse> getUserInfomation(
            @RequestParam("code") String code
    ) throws JsonProcessingException, AuthenticationException {
        String jwt =  kakaoService.getToken(code);
        return ResponseEntity.ok().body(new TokenResponse(jwt));
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

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleAlreadyExistsException(BadRequestException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("JoinError", "이미 존재하는 이메일입니다 다른 이메일을 사용해주세요");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleLoginFailException(NoSuchElementException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("LoginError", "로그인에 실패하셨습니다. 다시 시도해주세요");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
    }
}
