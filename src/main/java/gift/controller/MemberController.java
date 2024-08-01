package gift.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gift.dto.request.LoginRequest;
import gift.dto.request.RegisteRequest;
import gift.dto.response.TokenResponse;
import gift.service.MemberService;
import gift.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "member", description = "멤버 API")
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService, JwtUtil jwtUtil){
        this.memberService = memberService;
    }
    
    
    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "파라미터로 받은 회원의 회원가입을 진행합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "회원가입 성공"),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 회원")
    })
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisteRequest registeRequest, BindingResult bindingResult){
        memberService.addMember(registeRequest);
        String token = memberService.generateToken(registeRequest.getEmail());
        return new ResponseEntity<>(new TokenResponse(token), HttpStatus.CREATED);
    }


    @PostMapping("/login")
    @Operation(summary = "로그인", description = "파라미터로 받은 회원의 로그인을 진행합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공"),
        @ApiResponse(responseCode = "403", description = "이메일 혹은 비밀번호가 틀림")
    })
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult){
        String token = memberService.authenticateMember(loginRequest);
        return new ResponseEntity<>(new TokenResponse(token), HttpStatus.OK);
    }
}
