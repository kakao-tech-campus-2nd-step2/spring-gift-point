package gift.authentication.restapi;

import gift.advice.ErrorResponse;
import gift.authentication.restapi.dto.request.LoginRequest;
import gift.authentication.restapi.dto.request.SignUpRequest;
import gift.authentication.restapi.dto.response.LoginResponse;
import gift.core.domain.authentication.AuthenticationService;
import gift.core.domain.authentication.Token;
import gift.core.domain.user.User;
import gift.core.domain.user.UserAccount;
import gift.core.domain.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "인증/인가")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(
            AuthenticationService authenticationService,
            UserService userService
    ) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/api/members/login")
    @Operation(summary = "로그인 API", description = "이메일과 비밀번호로 로그인을 수행합니다.")
    @ApiResponses(
           value = {
                     @ApiResponse(
                            responseCode = "200",
                            description = "로그인 성공 시 액세스 토큰을 생성하여 반환합니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))
                     ),
                     @ApiResponse(
                            responseCode = "400",
                            description = "로그인 실패 시 반환합니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                     )
           }
    )
    public LoginResponse login(@RequestBody LoginRequest request) {
        Token token = authenticationService.authenticate(request.email(), request.password());
        return LoginResponse.of(token);
    }

    @PostMapping("/api/members/register")
    @Operation(summary = "회원가입 API", description = "이름, 이메일, 비밀번호로 회원가입을 수행합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원가입 성공 시 반환합니다."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "회원가입 실패 시 반환합니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public void signUp(@RequestBody SignUpRequest request) {
        userService.registerUser(userOf(request));
    }

    private User userOf(SignUpRequest request) {
        return new User(
                0L,
                request.name(),
                new UserAccount(
                        request.email(),
                        request.password()
                )
        );
    }
}
