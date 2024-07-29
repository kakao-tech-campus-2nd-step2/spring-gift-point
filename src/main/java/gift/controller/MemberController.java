package gift.controller;

import gift.domain.MemberRequest;
import gift.service.MemberService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/join")
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
