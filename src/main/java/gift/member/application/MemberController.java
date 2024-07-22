package gift.member.application;

import gift.member.application.dto.request.MemberSignUpRequest;
import gift.member.application.dto.response.MemberSignInResponse;
import gift.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "User관련 API")
@RestController
@RequestMapping("/api/users")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "회원 가입", description = "회원 가입을 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원 가입 성공"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 사용자"),
            @ApiResponse(responseCode = "404", description = "회원가입이 올바르게 이루어지지 않음")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<MemberSignInResponse> signUp(@RequestBody MemberSignUpRequest memberSignupRequest) {
        var savedUserInfo = memberService.signUp(memberSignupRequest.toServiceDto());

        var response = MemberSignInResponse.from(savedUserInfo);

        return ResponseEntity.created(URI.create("/api/users/" + savedUserInfo.id()))
                .body(response);
    }

    @Operation(summary = "로그인", description = "로그인을 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @PostMapping("/sign-in")
    public ResponseEntity<MemberSignInResponse> signIn(@RequestBody MemberSignUpRequest memberSignupRequest) {
        var signInUserInfo = memberService.signIn(memberSignupRequest.toServiceDto());

        var response = MemberSignInResponse.from(signInUserInfo);

        return ResponseEntity.ok()
                .body(response);
    }
}
