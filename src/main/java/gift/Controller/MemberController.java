package gift.Controller;

import gift.DTO.MemberDTO;
import gift.Service.MemberAccessTokenProvider;
import gift.Service.MemberService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {
    private final MemberService memberService;
    private final MemberAccessTokenProvider memberAccessTokenProvider;

    public MemberController(MemberService memberService, MemberAccessTokenProvider memberAccessTokenProvider){
        this.memberService = memberService;
        this.memberAccessTokenProvider = memberAccessTokenProvider;
    }

    @PostMapping("/api/members/register")
    public ResponseEntity<String> signupMember(@RequestBody MemberDTO memberDTO){
        memberService.signupMember(memberDTO);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/api/members/login")
    public ResponseEntity<String> loginMember(@RequestBody MemberDTO memberDTO){
        memberService.checkMember(memberDTO.getEmail());
        String token = memberAccessTokenProvider.createJwt(memberDTO.getEmail());
        return ResponseEntity.ok(token);
    }
}
