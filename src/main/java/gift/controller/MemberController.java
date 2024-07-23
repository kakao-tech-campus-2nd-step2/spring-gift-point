package gift.controller;

import gift.dto.MemberDto;
import gift.response.JwtResponse;
import gift.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody MemberDto memberDTO) {
        String newMemberEmail = memberService.registerMember(memberDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("새 멤버의 이메일: " + newMemberEmail);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody MemberDto memberDTO) {
        String jwt = memberService.login(memberDTO);
        return ResponseEntity.ok()
            .body(new JwtResponse(jwt));
    }
}
