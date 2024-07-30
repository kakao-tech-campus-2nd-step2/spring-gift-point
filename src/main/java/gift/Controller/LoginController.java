package gift.Controller;

import gift.Service.LoginService;
import gift.Service.MemberAccessTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Login", description = "Login 관련 API")
@RestController
public class LoginController {

    private final LoginService loginService;
    private final MemberAccessTokenProvider memberAccessTokenProvider;

    public LoginController(LoginService loginService,MemberAccessTokenProvider memberAccessTokenProvider){
        this.loginService = loginService;
        this.memberAccessTokenProvider = memberAccessTokenProvider;
    }

    @Operation(
        summary = "카카오 로그인 하여 jwt 토큰 전달",
        description = "카카오 로그인을 통해서 정보를 가져와 해당하는 정보를 통해 회원가입을 or 로그인을 하여 jwt 토큰을 발급"
    )
    @ApiResponse(
        responseCode = "200",
        description = "카카오 로그인 성공"
    )
    @Parameter(name = "code", description = "카카오 로그인을 통해 얻은 인가코드")
    @GetMapping("")
    public ResponseEntity<String> getLogin(@RequestParam(value = "code") String code){
        var response = loginService.makeResponse(code);
        String accessToken = loginService.abstractToken(response);
        String id = loginService.getId(accessToken);
        loginService.getMemberOrSignup(id, accessToken);
        String jwtToken = memberAccessTokenProvider.createJwt(id+"@kakao.com");
        return ResponseEntity.ok().body(jwtToken);
    }
}
