package gift.controller.restcontroller;

import gift.common.enums.Role;
import gift.controller.dto.request.SignUpRequest;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "SignUp", description = "회원가입 API")
@RestController
@RequestMapping("/api/members")
public class SignUpRestController {
    private final MemberService memberService;

    public SignUpRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "회원가입을 시도합니다.")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequest request) {
        memberService.signUp(request.email(), request.password(), request.name(), Role.USER);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
