package gift.controller;

import gift.auth.JwtService;
import gift.auth.LoginMember;
import gift.request.JoinRequest;
import gift.request.LoginMemberDto;
import gift.response.member.JoinResponse;
import gift.request.LoginRequest;
import gift.exception.InputException;
import gift.model.Member;
import gift.response.member.MemberInfoResponse;
import gift.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberApiController {

    private final MemberService memberService;
    private final JwtService jwtService;

    public MemberApiController(MemberService memberService, JwtService jwtService) {
        this.memberService = memberService;
        this.jwtService = jwtService;
    }

    @PostMapping("/api/members/register")
    public ResponseEntity<JoinResponse> join(@RequestBody @Valid JoinRequest joinRequest,
        BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            throw new InputException(bindingResult.getAllErrors());
        }

        Member joinedMember = memberService.join(joinRequest.email(), joinRequest.password());
        jwtService.addTokenInCookie(joinedMember, response);
        JoinResponse joinResponse = new JoinResponse(joinRequest.email(), "회원가입이 완료되었습니다.");

        return new ResponseEntity<>(joinResponse, HttpStatus.OK);
    }

    @PostMapping("/api/members/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest,
        BindingResult bindingResult, HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            throw new InputException(bindingResult.getAllErrors());
        }

        Member loginedMember = memberService.login(loginRequest.email(), loginRequest.password());
        jwtService.addTokenInCookie(loginedMember, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/members/me")
    public ResponseEntity<MemberInfoResponse> memberInfo(@LoginMember LoginMemberDto loginMemberDto) {
        MemberInfoResponse dto = memberService.getMemberInfo(loginMemberDto.id());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

}
