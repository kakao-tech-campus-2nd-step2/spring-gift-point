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
    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Member member) {
        memberService.save(member);
        Map<String, String> response = new HashMap<>();
        response.put("message", "success");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Member loginRequest) {
        Optional<Member> memberOpt = memberService.findByEmail(loginRequest.getEmail());
        if(memberOpt.isPresent() && memberOpt.get().getPassword().equals(loginRequest.getPassword())) {
            String token = jwtUtil.generateToken(memberOpt.get().getEmail());
            Map<String, String> response = new HashMap<>();
            response.put("message", "success");
            response.put("token", token);
            return ResponseEntity.ok(response);
        }
        return  ResponseEntity.status(401).body(null);
    }

    // endpoint that checking member information
    @GetMapping("/me")
    public ResponseEntity<Member> getCurrentMember(HttpServletRequest request) {
        String email = getEmailFromRequest(request);
        if(email != null) {
            Optional<Member> memberOpt = memberService.findByEmail(email);
            if(memberOpt.isPresent()) {
                return ResponseEntity.ok(memberOpt.get());
            }
        }
        return ResponseEntity.status(401).body(null);
    }

    // Extracting email from token using HttpServletRequest
    private String getEmailFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return jwtUtil.extractEmail(token);
        }
        return null;
    }
}
