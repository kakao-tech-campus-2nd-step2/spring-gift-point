package gift.Controller;

import gift.DTO.MemberDTO;
import gift.Service.MemberAccessTokenProvider;
import gift.Service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member", description = "Member 관련 API")
@RestController
public class MemberController {
    private final MemberService memberService;
    private final MemberAccessTokenProvider memberAccessTokenProvider;

    public MemberController(MemberService memberService, MemberAccessTokenProvider memberAccessTokenProvider){
        this.memberService = memberService;
        this.memberAccessTokenProvider = memberAccessTokenProvider;
    }
    @Operation(
        summary = "회원 가입",
        description = "전달된 정보로 회원가입"
    )
    @ApiResponse(
        responseCode = "201",
        description = "회원가입 성공"
    )
    @Parameter(name = "memberDTO", description = "회원 정보")
    @PostMapping("/api/members/register")
    public ResponseEntity<String> signupMember(@Valid @RequestBody MemberDTO memberDTO){
        memberService.signupMember(memberDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(("User registered successfully"));
    }
    @Operation(
        summary = "회원 로그인",
        description = "전달된 정보로 로그인"
    )
    @ApiResponse(
        responseCode = "200",
        description = "로그인 성공"
    )
    @Parameter(name = "memberDTO", description = "회원 정보")
    @PostMapping("/api/members/login")
    public ResponseEntity<String> loginMember(@Valid @RequestBody MemberDTO memberDTO){
        memberService.checkMember(memberDTO);
        String token = memberAccessTokenProvider.createJwt(memberDTO.getEmail());
        return ResponseEntity.ok(token);
    }
}
