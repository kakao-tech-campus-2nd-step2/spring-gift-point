package gift.controller;

import gift.dto.MemberDto;
import gift.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController (MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<MemberDto> register(@RequestBody MemberDto memberDto) {
        MemberDto registeredMember = memberService.register(memberDto);
        return ResponseEntity.status(201).body(registeredMember);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberDto> login(@RequestBody MemberDto memberDto) {
        try {
            MemberDto loggedInMember = memberService.login(memberDto.getEmail(), memberDto.getPassword());
            return ResponseEntity.ok(loggedInMember);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(new MemberDto("잘못된 인증입니다."));
        }
    }
}
