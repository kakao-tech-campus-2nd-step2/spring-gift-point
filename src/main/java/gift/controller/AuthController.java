package gift.controller;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@Tag(name = "auth", description = "인증 API")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final MemberService memberService;

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("")
    @Operation(summary = "인증 메인화면", description = "인증과정의 메인 화면으로 이동합니다.")
    public String main() {
        return "auth/index";
    }

    @GetMapping("/login")
    @Operation(summary = "로그인 화면", description = "로그인 화면으로 이동합니다.")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/sign-up")
    @Operation(summary = "회원가입 화면", description = "회원가입 화면으로 이동합니다.")
    public String signUp() {
        return "auth/sign-up";
    }

    @PostMapping("/save")
    @Operation(summary = "회원정보 저장", description = "회원정보 값(id,email)을 입력하면 회원정보를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원정보 저장 성공", content = @Content(schema = @Schema(implementation = MemberResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<MemberResponseDto> save(@ModelAttribute MemberRequestDto memberRequestDto) {
        MemberResponseDto memberResponseDto = memberService.save(memberRequestDto.getEmail(), memberRequestDto.getPassword());
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }

    @PostMapping("/user/login")
    @Operation(summary = "로그인", description = "(id,email)을 입력하면 로그인 로직을 수행합니다.")
    public ResponseEntity<String> login(@ModelAttribute MemberRequestDto memberRequestDto) throws AuthenticationException {
        if (memberService.login(memberRequestDto.getEmail(), memberRequestDto.getPassword())) {
            return new ResponseEntity<>(memberService.generateTokenFrom(memberRequestDto.getEmail()), HttpStatus.OK);
        }
        throw new AuthenticationException("로그인 실패");
    }
}
