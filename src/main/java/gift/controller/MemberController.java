package gift.controller;

import gift.model.MemberDTO;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    @Operation(
        summary = "회원 가입",
        description = "새로운 멤버를 생성합니다. 요청 본문에 멤버 정보가 필요합니다.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "회원 가입을 위한 멤버 정보",
            required = true,
            content = @Content(schema = @Schema(implementation = MemberDTO.class))
        )
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "회원이 성공적으로 생성되었습니다.",
            content = @Content(schema = @Schema(implementation = MemberDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "입력값이 유효하지 않습니다.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "내부 서버 에러입니다.",
            content = @Content
        )
    })
    public ResponseEntity<?> createMember(
        @Parameter(description = "회원 가입을 위한 멤버 정보", required = true)
        @RequestBody MemberDTO memberDTO) {
        MemberDTO createdMember = memberService.createMember(memberDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMember);
    }

    @PostMapping("/login")
    @Operation(
        summary = "회원 로그인",
        description = "회원 인증 후 JWT 토큰을 생성합니다. 요청 본문에 로그인 정보가 필요합니다.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "로그인을 위한 멤버 정보 (이메일과 비밀번호)",
            required = true,
            content = @Content(schema = @Schema(implementation = MemberDTO.class))
        )
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "로그인 성공. JWT 토큰을 반환합니다.",
            content = @Content(schema = @Schema(type = "string", example = "Bearer some_jwt_token"))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "잘못된 이메일 또는 비밀번호",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "내부 서버 에러입니다.",
            content = @Content
        )
    })
    public ResponseEntity<?> loginMember(
        @Parameter(description = "로그인을 위한 멤버 정보", required = true)
        @RequestBody MemberDTO memberDTO) {
        MemberDTO member = memberService.findMemberByCredentials(memberDTO.email(),
            memberDTO.password());
        String token = memberService.generateToken(member.id());
        return ResponseEntity.ok("Bearer " + token);
    }
}
