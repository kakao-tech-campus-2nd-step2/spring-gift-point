package gift.controller;

import gift.dto.member.LoginResponse;
import gift.dto.member.MemberRequest;
import gift.dto.member.MemberResponse;
import gift.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import gift.service.MemberService;
import gift.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

import java.net.URI;
import java.util.Optional;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Member Management System", description = "Operations related to member management")
public class MemberController {
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @Autowired
    public MemberController(MemberService service, JwtUtil util) {
        this.memberService = service;
        this.jwtUtil = util;
    }

    @PostMapping(value ="/register")
    @Operation(summary = "Register a new member", description = "Registers a new member to the system", tags = { "Member Management System" })
    public ResponseEntity<MemberResponse> register(
            @Parameter(description = "Member object to be registered", required = true)
            @RequestBody MemberRequest memberRequest) {
        Member member = new Member.Builder()
                .email(memberRequest.getEmail())
                .password(memberRequest.getPassword())
                .name(memberRequest.getName())
                .build();
        member = memberService.save(member);
        String token = jwtUtil.generateToken(member.getEmail());
        MemberResponse memberResponse = new MemberResponse(member.getId(), member.getEmail(), member.getName(), token);

        return ResponseEntity.created(URI.create("/api/members/" + member.getId())).body(memberResponse);
    }

    @PostMapping("/login")
    @Operation(summary = "Login a member", description = "Logs in an existing member", tags = { "Member Management System" })
    public ResponseEntity<LoginResponse> login(
            @Parameter(description = "Login request containing email and password", required = true)
            @RequestBody MemberRequest loginRequest) {
        Optional<Member> memberOpt = memberService.findByEmail(loginRequest.getEmail());
        if (memberOpt.isPresent() && memberOpt.get().getPassword().equals(loginRequest.getPassword())) {
            String token = jwtUtil.generateToken(memberOpt.get().getEmail());
            LoginResponse response = new LoginResponse(token);

            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
