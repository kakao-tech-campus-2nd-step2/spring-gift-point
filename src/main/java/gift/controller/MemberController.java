package gift.controller;

import gift.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "멤버", description = "사용자 관련 API")
@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> signUp(@RequestHeader("Authorization") String str) {
        var token = memberService.signUp(str);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestHeader("Authorization") String str) {
        var token = memberService.login(str);
        return ResponseEntity.ok(token);
    }
}
