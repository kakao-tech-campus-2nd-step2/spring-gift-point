package gift.controller;

import gift.classes.RequestState.SecureRequestStateDTO;
import gift.dto.RequestMemberDto;
import gift.dto.TokenDto;
import gift.services.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@Tag(name = "MemberController", description = "Member API")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    @Operation(summary = "회원 가입", description = "새로운 회원을 등록하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원 가입 성공"),
        @ApiResponse(responseCode = "400", description = "회원 가입 실패(이메일 중복)"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    public ResponseEntity<?> register(@RequestBody RequestMemberDto requestMemberDto) {
        memberService.register(requestMemberDto);
        TokenDto tokenDto = memberService.login(requestMemberDto);

        return ResponseEntity.ok().body(new SecureRequestStateDTO(
            HttpStatus.OK,
            "회원가입에 성공했습니다.",
            tokenDto
        ));
    }

    @PostMapping("/login")
    @Operation(summary = "회원 로그인", description = "회원이 로그인하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공"),
        @ApiResponse(responseCode = "401", description = "인증 실패"),
        @ApiResponse(responseCode = "404", description = "해당 아이디가 존재하지 않음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    public ResponseEntity<?> login(@RequestBody RequestMemberDto requestMemberDto) {
        TokenDto tokenDto = memberService.login(requestMemberDto);

        return ResponseEntity.ok().body(new SecureRequestStateDTO(
            HttpStatus.OK,
            "로그인에 성공했습니다.",
            tokenDto
        ));
    }
}
