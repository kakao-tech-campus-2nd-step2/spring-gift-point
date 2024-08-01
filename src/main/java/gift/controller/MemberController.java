package gift.controller;

import gift.dto.MemberDTO;
import gift.model.Member;
import gift.service.MemberService;
import gift.util.JwtUtil;
import gift.util.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @Autowired
    public MemberController(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "회원 가입")
    @PostMapping("/register")
    public ResponseEntity<?> registerMember(@Valid @RequestBody MemberDTO memberDTO) {
        MemberDTO savedMember = memberService.register(memberDTO);
        String token = jwtUtil.generateToken(savedMember.getEmail());
        return ResponseEntity.ok().body("{\"token\":\"" + token + "\"}");
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody MemberDTO loginDetails) {
        String email = loginDetails.getEmail();
        String password = loginDetails.getPassword();
        String token = memberService.login(email, password);
        return ResponseEntity.ok().body("{\"token\":\"" + token + "\"}");
    }

    @Operation(summary = "회원 프로필 조회")
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@LoginMember Member member) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(member.getId());
        memberDTO.setEmail(member.getEmail());
        memberDTO.setPassword(member.getPassword());
        return ResponseEntity.ok(memberDTO);
    }
}
