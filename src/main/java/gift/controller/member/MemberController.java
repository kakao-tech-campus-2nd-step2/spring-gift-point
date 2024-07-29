package gift.controller.member;

import gift.controller.member.dto.MemberRequest;
import gift.controller.member.dto.MemberResponse;
import gift.global.auth.Authenticate;
import gift.global.auth.Authorization;
import gift.global.auth.LoginInfo;
import gift.model.member.Role;
import gift.application.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "유저 회원가입", description = "유저 회원가입 api")
    @PostMapping("/register")
    public ResponseEntity<String> register(
        @RequestBody @Valid MemberRequest.Register request
    ) {
        memberService.register(request.toCommand());
        return ResponseEntity.ok("User created successfully.");
    }

    @Operation(summary = "유저 로그인", description = "유저 로그인 api")
    @PostMapping("/login")
    public ResponseEntity<MemberResponse.Login> login(
        @RequestBody @Valid MemberRequest.Login request
    ) {
        var response = memberService.login(request.toCommand());
        return ResponseEntity.ok(MemberResponse.Login.from(response));
    }

    @Operation(summary = "유저 로그아웃", description = "유저 로그아웃 api")
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return null;
    }

    @Operation(summary = "유저 정보 조회", description = "유저 정보 조회 api")
    @Authorization(role = Role.USER)
    @GetMapping("")
    public ResponseEntity<MemberResponse.Info> getUser(@Authenticate LoginInfo loginInfo) {
        var response = memberService.getUser(loginInfo.memberId());
        return ResponseEntity.ok(MemberResponse.Info.from(response));
    }

}
