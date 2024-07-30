package gift.controller;

import gift.dto.ApiResponse;
import gift.model.Member;
import gift.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody Member member) {
        return memberService.registerMember(member)
            .map(token -> {
                ApiResponse<String> response = new ApiResponse<>(true, "Member registered successfully", token, null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            })
            .orElseGet(() -> {
                ApiResponse<String> response = new ApiResponse<>(false, "Registration failed", null, "500");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            });
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody Member member) {
        return memberService.login(member.getEmail(), member.getPassword())
            .map(token -> {
                ApiResponse<String> response = new ApiResponse<>(true, "Login successful", token, null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            })
            .orElseGet(() -> {
                ApiResponse<String> response = new ApiResponse<>(false, "Invalid email or password", null, "403");
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
            });
    }
}
