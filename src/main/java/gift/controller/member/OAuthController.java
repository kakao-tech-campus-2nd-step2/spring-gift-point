package gift.controller.member;

import gift.application.member.MemberFacade;
import gift.controller.member.dto.MemberResponse;
import gift.controller.member.dto.OAuthRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthController {

    private final MemberFacade memberFacade;

    public OAuthController(MemberFacade memberFacade) {
        this.memberFacade = memberFacade;
    }

    @Operation(summary = "소셜 로그인", description = "소셜 로그인 api")
    @PostMapping("/oauth/login")
    public ResponseEntity<MemberResponse.Login> login(
        @RequestBody @Valid OAuthRequest.LoginRequest request
    ) {
        var response = memberFacade.socialLogin(request.toCommand());
        return ResponseEntity.ok(MemberResponse.Login.from(response));
    }
}
