package gift.member.controller;

import gift.global.argumentresolver.LoginMember;
import gift.member.dto.MemberDto;
import gift.member.dto.MemberPasswordRequest;
import gift.member.dto.MemberResponse;
import gift.member.entity.Member;
import gift.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member", description = "회원 관련 API")
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "회원가입", description = "회원가입을 합니다.")
    @PostMapping("/register")
    public ResponseEntity<MemberResponse> register(@Valid @RequestBody MemberDto memberDto) {
        return ResponseEntity.ok().body(memberService.register(memberDto));
    }

    @Operation(summary = "로그인", description = "로그인을 합니다.")
    @PostMapping("/login")
    public ResponseEntity<MemberResponse> login(@Valid @RequestBody MemberDto memberDto) {
        return ResponseEntity.ok().body(memberService.login(memberDto));
    }

    @Operation(summary = "비밀번호 변경", description = "해당 회원의 비밀번호를 변경합니다.")
    @PostMapping("/password")
    public ResponseEntity<MemberResponse> changePassword(
        @LoginMember Member member,
        @Valid @RequestBody MemberPasswordRequest memberPasswordRequest
    ) {
        return ResponseEntity.ok().body(memberService.changePassword(member, memberPasswordRequest));
    }
}
