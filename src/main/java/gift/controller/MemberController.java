package gift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.annotation.LoginMember;
import gift.dto.ErrorResponseDto;
import gift.dto.MemberLoginDto;
import gift.dto.MemberRegisterDto;
import gift.entity.PointResponseEntity;
import gift.entity.TokenResponseEntity;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Member API", description = "회원 관리 API")
public class MemberController {

    private final MemberService memberService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "회원 가입", description = "새로운 회원을 등록한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "회원 가입 성공", content = @Content(schema = @Schema(implementation = MemberRegisterDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<String> signUp(@RequestBody @Valid MemberLoginDto memberLoginDto)
        throws Exception {
        String generatedToken = memberService.registerMember(memberLoginDto.email,
            memberLoginDto.password);
        return new ResponseEntity<>(
            objectMapper.writeValueAsString(new TokenResponseEntity(generatedToken)),
            HttpStatus.CREATED);
    }

    @Operation(summary = "회원 로그인", description = "사용자 로그인 처리를 하고 성공 시 JWT를 응답한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = MemberLoginDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "403", description = "이메일 혹은 비밀번호 불일치", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<String> signIn(@RequestBody @Valid MemberRegisterDto memberRegisterDto)
        throws Exception {
        String generatedToken = memberService.authenticateMember(memberRegisterDto.email,
            memberRegisterDto.password);
        return new ResponseEntity<>(
            objectMapper.writeValueAsString(new TokenResponseEntity(generatedToken)),
            HttpStatus.OK);
    }

    @Operation(summary = "포인트 조회", description = "로그인한 사용자의 보유 포인트를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "포인트 조회 성공"),
        @ApiResponse(responseCode = "401", description = "인증 필요", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/points")
    public ResponseEntity<String> getPoint(@LoginMember Long memberId) throws Exception {
        Integer point = memberService.getPointByMemberId(memberId);
        return new ResponseEntity<>(
            objectMapper.writeValueAsString(new PointResponseEntity(point)), HttpStatus.OK
        );
    }
}