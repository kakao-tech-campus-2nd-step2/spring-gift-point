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

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/member")
@Tag(name = "Member Management System", description = "Operations related to member management")
public class MemberController {
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @Autowired
    public MemberController(MemberService service, JwtUtil util) {
        this.memberService = service;
        this.jwtUtil = util;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new member", description = "Registers a new member to the system", tags = { "Member Management System" })
    public ResponseEntity<Map<String, String>> register(
            @Parameter(description = "Member object to be registered", required = true)
            @RequestBody Member member) {
        memberService.save(member);
        Map<String, String> response = new HashMap<>();
        response.put("message", "success");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Login a member", description = "Logs in an existing member", tags = { "Member Management System" })
    public ResponseEntity<Map<String, String>> login(
            @Parameter(description = "Login request containing email and password", required = true)
            @RequestBody Member loginRequest) {
        Optional<Member> memberOpt = memberService.findByEmail(loginRequest.getEmail());
        if (memberOpt.isPresent() && memberOpt.get().getPassword().equals(loginRequest.getPassword())) {
            String token = jwtUtil.generateToken(memberOpt.get().getEmail());
            Map<String, String> response = new HashMap<>();
            response.put("message", "success");
            response.put("token", token);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).body(null);
    }

    @GetMapping("/me")
    @Operation(summary = "Get current logged-in member information", description = "Fetches information about the currently logged-in member", tags = { "Member Management System" })
    public ResponseEntity<Member> getCurrentMember(HttpServletRequest request) {
        String email = getEmailFromRequest(request);
        if (email != null) {
            Optional<Member> memberOpt = memberService.findByEmail(email);
            if (memberOpt.isPresent()) {
                return ResponseEntity.ok(memberOpt.get());
            }
        }
        return ResponseEntity.status(401).body(null);
    }

    private String getEmailFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return jwtUtil.extractEmail(token);
        }
        return null;
    }
}
