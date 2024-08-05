package gift.member.presentation;

import gift.exception.AlreadyExistMember;
import gift.exception.NotFoundMember;
import gift.member.application.dto.PointResponseDto;
import gift.member.application.dto.TokenResponseDto;
import gift.member.application.service.LogoutTokenService;
import gift.member.application.service.MemberService;
import gift.member.application.dto.RegisterResponseDto;
import gift.member.persistence.entity.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/points")
    public PointResponseDto getPoint(@LoginMember Member member) {
        return memberService.getPoint(member);
    }

}
