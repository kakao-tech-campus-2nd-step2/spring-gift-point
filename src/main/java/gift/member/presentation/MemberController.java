package gift.member.presentation;

import gift.exception.AlreadyExistMember;
import gift.exception.NotFoundMember;
import gift.member.application.dto.TokenResponseDto;
import gift.member.application.service.LogoutTokenService;
import gift.member.application.service.MemberService;
import gift.member.application.dto.RegisterResponseDto;
import gift.member.persistence.Member;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/members")
public class MemberController {

    private final MemberService memberService;
    private final LogoutTokenService logoutTokenService;

    public MemberController(MemberService memberService, LogoutTokenService logoutTokenService) {
        this.memberService = memberService;
        this.logoutTokenService = logoutTokenService;
    }

    @PostMapping("/register")
    public RegisterResponseDto registerMember(@RequestBody Member member) throws AlreadyExistMember {
        return memberService.register(member);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody Member member) throws NotFoundMember {
        return memberService.login(member);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        logoutTokenService.postToken(token);
        return ResponseEntity.ok("로그아웃 되었습니다");
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    public static @interface LoginMember {

    }
}
