package gift.controller;

import static gift.util.constants.MemberConstants.EMAIL_ALREADY_USED;
import static gift.util.constants.MemberConstants.INVALID_CREDENTIALS;

import gift.dto.member.MemberAuthResponse;
import gift.dto.member.MemberLoginRequest;
import gift.dto.member.MemberRegisterRequest;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member API", description = "회원 관리 API")
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "(명세 통일) 회원 가입", description = "새로운 회원을 등록합니다.")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(
                responseCode = "409",
                description = "이미 존재하는 이메일",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"error\": \"" + EMAIL_ALREADY_USED + "\"}")
                )
            ),
            @ApiResponse(
                responseCode = "500",
                description = "서버 오류",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"error\": \"(서버 오류 메시지)\"}")
                )
            )
        }
    )
    @PostMapping("/register")
    public ResponseEntity<MemberAuthResponse> register(
        @Valid @RequestBody MemberRegisterRequest memberRegisterRequest
    ) {
        MemberAuthResponse registeredMember = memberService.registerMember(memberRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredMember);
    }

    @Operation(summary = "(명세 통일) 회원 로그인", description = "회원 로그인을 처리합니다.")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201", description = "로그인 성공"),
            @ApiResponse(
                responseCode = "403",
                description = "잘못된 입력",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"error\": \"" + INVALID_CREDENTIALS + "\"}")
                )
            ),
            @ApiResponse(
                responseCode = "500",
                description = "서버 오류",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"error\": \"(서버 오류 메시지)\"}")
                )
            )
        }
    )
    @PostMapping("/login")
    public ResponseEntity<MemberAuthResponse> login(
        @Valid @RequestBody MemberLoginRequest memberLoginRequest
    ) {
        MemberAuthResponse loggedInMember = memberService.loginMember(memberLoginRequest);
        return ResponseEntity.ok(loggedInMember);
    }
}
