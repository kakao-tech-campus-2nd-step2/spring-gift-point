package gift.controller;

import gift.entity.Member;
import gift.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import gift.service.MemberService;
import gift.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @Autowired
    public MemberController(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Member member) {
        memberService.save(member);
        Map<String, String> response = new HashMap<>();
        response.put("message", "success");
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> login(@RequestBody Member loginRequest) {
        Member member = memberService.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new CustomException.InvalidCredentialsException("Invalid credentials"));

        if(!member.getPassword().equals(loginRequest.getPassword())) {
            throw new CustomException.InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(member.getEmail());
        Map<String, String> response = new HashMap<>();
        response.put("message", "success");
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentMember(HttpServletRequest request) {
        String email = jwtUtil.getEmailFromRequest(request);
        if(email == null) {
            throw new CustomException.InvalidCredentialsException("Invalid token");
        }
        Member member = memberService.findByEmail(email).orElseThrow(() -> new CustomException.EntityNotFoundException("Member not found"));
        return ResponseEntity.ok(member);
    }

}
