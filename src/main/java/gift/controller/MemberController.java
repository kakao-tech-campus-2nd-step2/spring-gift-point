package gift.controller;

import gift.dto.MemberDto;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name="회원 API")
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController (MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "회원가입 후 토큰 받기")
    @PostMapping("/register")
    public ResponseEntity<MemberDto> register(@RequestBody MemberDto memberDto) {
        MemberDto registeredMember = memberService.register(memberDto);
        return ResponseEntity.status(201).body(registeredMember);
    }
    
    @Operation(summary = "로그인하고 토큰 받기")
    @PostMapping("/login")
    public ResponseEntity<MemberDto> login(@RequestBody MemberDto memberDto) {
        try {
            MemberDto loggedInMember = memberService.login(memberDto.getEmail(), memberDto.getPassword());
            return ResponseEntity.ok(loggedInMember);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(new MemberDto("잘못된 토큰 인증입니다."));
        }
    }
}
