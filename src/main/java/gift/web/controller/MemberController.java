package gift.web.controller;

import gift.service.member.MemberService;
import gift.web.dto.MemberDto;
import gift.web.dto.Token;
import gift.web.jwt.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/members")
public class MemberController {
    private final MemberService memberService;
    private final JwtUtils jwtUtils;

    public MemberController(MemberService memberService, JwtUtils jwtUtils) {
        this.memberService = memberService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerMember(@Valid @RequestBody MemberDto memberDto) {
        memberService.createMember(memberDto);
        return new ResponseEntity<>(new Token(jwtUtils.createJWT(memberDto)).makeTokenHeader(), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginMember(@Valid @RequestBody MemberDto memberDto) {
        memberService.loginValidate(memberDto);
        return new ResponseEntity<>(new Token(jwtUtils.createJWT(memberDto)).makeTokenHeader(), HttpStatus.OK);
    }

    @PutMapping("/{email}")
    public ResponseEntity<MemberDto> updateMember(@PathVariable String email, @RequestBody MemberDto memberDto) {
        return new ResponseEntity<>(memberService.updateMember(email, memberDto), HttpStatus.OK);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<?> deleteMember(@PathVariable String email) {
        memberService.deleteMember(email);
        return ResponseEntity.ok("Delete Success");
    }
}
