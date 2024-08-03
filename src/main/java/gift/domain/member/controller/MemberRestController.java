package gift.domain.member.controller;

import gift.auth.jwt.JwtToken;
import gift.config.LoginUser;
import gift.domain.member.dto.MemberLoginRequest;
import gift.domain.member.dto.MemberLoginResponse;
import gift.domain.member.dto.MemberRequest;
import gift.domain.member.dto.PointRechargeRequest;
import gift.domain.member.dto.PointResponse;
import gift.domain.member.entity.Member;
import gift.domain.member.service.MemberService;
import gift.exception.DuplicateEmailException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Member", description = "회원 API")
public class MemberRestController {

    private final MemberService memberService;

    public MemberRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    @Operation(summary = "회원 가입", description = "회원 정보를 등록합니다.")
    public ResponseEntity<MemberLoginResponse> create(
        @Parameter(description = "회원 등록 정보", required = true)
        @RequestBody @Valid MemberRequest memberRequest
    ) {
        try {
            JwtToken jwtToken = memberService.signUp(memberRequest);
            MemberLoginResponse response = new MemberLoginResponse(memberRequest.email(), jwtToken.token());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DuplicateKeyException e) {
            throw new DuplicateEmailException("error.duplicate.key.email");
        }
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "회원 정보를 통해 로그인합니다.")
    public ResponseEntity<MemberLoginResponse> login(
        @Parameter(description = "회원 로그인 정보", required = true)
        @RequestBody @Valid MemberLoginRequest memberLoginRequest
    ) {
        JwtToken jwtToken = memberService.login(memberLoginRequest);
        MemberLoginResponse response = new MemberLoginResponse(memberLoginRequest.email(), jwtToken.token());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/point")
    @Operation(summary = "포인트 충전", description = "회원 포인트를 충전합니다.")
    public ResponseEntity<PointResponse> rechargePoint(
        @Parameter(description = "충전할 포인트 양", required = true)
        @RequestBody @Valid PointRechargeRequest pointRechargeRequest,
        @Parameter(hidden = true)
        @LoginUser Member member
    ) {
        PointResponse pointResponse = memberService.rechargePoint(pointRechargeRequest, member);
        return ResponseEntity.status(HttpStatus.OK).body(pointResponse);
    }

    @GetMapping("/point")
    @Operation(summary = "포인트 조회", description = "회원 포인트를 조회합니다.")
    public ResponseEntity<PointResponse> readPoint(
        @Parameter(hidden = true)
        @LoginUser Member member
    ) {
        PointResponse pointResponse = memberService.readPoint(member);
        return ResponseEntity.status(HttpStatus.OK).body(pointResponse);
    }
}
