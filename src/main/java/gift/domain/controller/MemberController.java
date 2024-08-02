package gift.domain.controller;

import gift.domain.controller.apiResponse.MemberLoginApiResponse;
import gift.domain.controller.apiResponse.MemberRegisterApiResponse;
import gift.domain.dto.request.member.MemberRequest;
import gift.domain.service.member.MemberService;
import gift.global.apiResponse.SuccessApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<MemberRegisterApiResponse> registerMember(@Valid @RequestBody MemberRequest requestDto) {
        var created = HttpStatus.CREATED;
        return SuccessApiResponse.of(new MemberRegisterApiResponse(created, memberService.registerMember(requestDto).token()), created);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberLoginApiResponse> loginMember(@Valid @RequestBody MemberRequest requestDto) {
        return SuccessApiResponse.ok(new MemberLoginApiResponse(HttpStatus.OK, memberService.loginMember(requestDto).token()));
    }
}
