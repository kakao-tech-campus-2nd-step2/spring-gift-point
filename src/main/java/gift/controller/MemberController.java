package gift.controller;

import static gift.util.constants.MemberConstants.EMAIL_ALREADY_USED;
import static gift.util.constants.MemberConstants.INVALID_CREDENTIALS;

import gift.config.CommonApiResponses.CommonForbiddenResponse;
import gift.config.CommonApiResponses.CommonServerErrorResponse;
import gift.config.CommonApiResponses.CommonUnauthorizedResponse;
import gift.dto.member.MemberAuthResponse;
import gift.dto.member.MemberLoginRequest;
import gift.dto.member.MemberPointResponse;
import gift.dto.member.MemberRegisterRequest;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
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
            )
        }
    )
    @CommonServerErrorResponse
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
                description = "email 또는 패스워드 불일치",
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"error\": \"" + INVALID_CREDENTIALS + "\"}")
                )
            )
        }
    )
    @CommonServerErrorResponse
    @PostMapping("/login")
    public ResponseEntity<MemberAuthResponse> login(
        @Valid @RequestBody MemberLoginRequest memberLoginRequest
    ) {
        MemberAuthResponse loggedInMember = memberService.loginMember(memberLoginRequest);
        return ResponseEntity.ok(loggedInMember);
    }

    @Operation(summary = "(명세 통일) 개인 포인트 조회", description = "개인의 포인트를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @CommonUnauthorizedResponse
    @CommonForbiddenResponse
    @CommonServerErrorResponse
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/point")
    public ResponseEntity<MemberPointResponse> getPoints(@RequestAttribute("memberId") Long memberId) {
        MemberPointResponse memberPointResponse = memberService.getPoints(memberId);
        return ResponseEntity.ok(memberPointResponse);
    }
}
