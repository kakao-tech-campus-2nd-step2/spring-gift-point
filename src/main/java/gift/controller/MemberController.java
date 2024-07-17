package gift.controller;

import gift.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import gift.service.MemberService;
import gift.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


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
        Optional<Member> memberOpt = memberService.findByEmail(loginRequest.getEmail());
        if(memberOpt.isPresent() && memberOpt.get().getPassword().equals(loginRequest.getPassword())) {
            String token = jwtUtil.generateToken(memberOpt.get().getEmail());
            Map<String, String> response = new HashMap<>();
            response.put("message", "success");
            response.put("token", token);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentMember(HttpServletRequest request) {
        String email = jwtUtil.getEmailFromRequest(request);
        if(email != null) {
            return memberService.findByEmail(email).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(401).build());
        }
        return ResponseEntity.status(401).body("Invalid token");
    }

}
