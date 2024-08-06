package gift.controller.member;

import gift.config.LoginUser;
import gift.controller.auth.AuthController;
import gift.controller.auth.LoginRequest;
import gift.controller.auth.LoginResponse;
import gift.controller.auth.Token;
import gift.controller.response.ApiResponseBody;
import gift.controller.response.ApiResponseBuilder;
import gift.service.AuthService;
import gift.service.MemberService;
import gift.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Member", description = "Member API")
@RequestMapping("api/members")
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;

    public MemberController(MemberService memberService, AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @GetMapping
    @Operation(summary = "get All Members", description = "모든 멤버 불러오기(기본 개수 : 5개)")
    public ResponseEntity<ApiResponseBody<Page<MemberResponse>>> getAllMembers(
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ApiResponseBuilder<Page<MemberResponse>>()
            .httpStatus(HttpStatus.OK)
            .data(memberService.findAll(pageable))
            .messages("모든 멤버 조회")
            .build();
    }

    @GetMapping("/{memberId}")
    @Operation(summary = "get Member", description = "memberId를 통해 멤버 정보 불러오기")
    public ResponseEntity<ApiResponseBody<MemberResponse>> getMember(@LoginUser LoginResponse member,
        @PathVariable UUID memberId) {
        AuthController.validateUserOrAdmin(member, memberId);
        return new ApiResponseBuilder<MemberResponse>()
            .httpStatus(HttpStatus.OK)
            .data((memberService.findById(memberId)))
            .messages("멤버 조회")
            .build();
    }

    @PostMapping("/register")
    @Operation(summary = "create Member", description = "멤버 생성(회원가입)")
    public ResponseEntity<ApiResponseBody<Token>> createMember(@Valid @RequestBody SignUpRequest member) {
        MemberResponse target = memberService.save(member);
        return new ApiResponseBuilder<Token>()
            .httpStatus(HttpStatus.OK)
            .data(new Token(JwtUtil.generateToken(target.id(), target.email())))
            .messages("회원가입")
            .build();
    }

    @PutMapping("/{memberId}")
    @Operation(summary = "modify Member", description = "멤버 정보 수정")
    public ResponseEntity<ApiResponseBody<MemberResponse>> updateMember(@LoginUser LoginResponse loginMember,
        @PathVariable UUID memberId, @RequestBody MemberRequest member) {
        AuthController.validateUserOrAdmin(loginMember, memberId);
        return new ApiResponseBuilder<MemberResponse>()
            .httpStatus(HttpStatus.OK)
            .data((memberService.update(memberId, member)))
            .messages("멤버 수정")
            .build();
    }

    @PutMapping("/point")
    @Operation(summary = "modify Member", description = "포인트 충전")
    public ResponseEntity<ApiResponseBody<String>> chargePoint(@LoginUser LoginResponse loginMember,
        @PathVariable UUID memberId, @RequestBody Long point) {
        AuthController.validateUserOrAdmin(loginMember, memberId);
        return new ApiResponseBuilder<String>()
            .httpStatus(HttpStatus.OK)
            .data("충전 후 잔액 : " + (memberService.chargePoint(memberId, point)))
            .messages("포인트 충전")
            .build();
    }

    @DeleteMapping("/{memberId}")
    @Operation(summary = "delete Member", description = "멤버 삭제")
    public ResponseEntity<ApiResponseBody<Void>> deleteMember(@LoginUser LoginResponse loginMember,
        @PathVariable UUID memberId) {
        AuthController.validateUserOrAdmin(loginMember, memberId);
        memberService.delete(memberId);
        return new ApiResponseBuilder<Void>()
            .httpStatus(HttpStatus.OK)
            .data(null)
            .messages("회원 탈퇴")
            .build();
    }
}