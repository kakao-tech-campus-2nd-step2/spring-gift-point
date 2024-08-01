package gift.controller;

import gift.config.LoginMember;
import gift.domain.member.Member;
import gift.dto.MemberDto;
import gift.dto.request.LoginRequest;
import gift.dto.request.RegisterRequest;
import gift.dto.response.ErrorResponse;
import gift.dto.response.JwtResponse;
import gift.dto.response.PointResponse;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member API", description = "회원 관련 API")
@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "회원 등록", description = "새로운 사용자를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "중복된 이메일로 인한 등록 실패"),
    })
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest request) {
        MemberDto memberDto = new MemberDto(request.getEmail(), request.getPassword());

        memberService.addMember(memberDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "회원 로그인", description = "사용자 로그인 처리를 하고, 성공 시 JWt를 응답합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 데이터", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "로그인 실패, 잘못된 자격 증명", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginRequest request) {
        MemberDto memberDto = new MemberDto(request.getEmail(), request.getPassword());

        String jwt = memberService.login(memberDto);

        return ResponseEntity.ok()
                .body(new JwtResponse(jwt));
    }

    @GetMapping("/point")
    public ResponseEntity<PointResponse> point(@LoginMember Member member) {
        return ResponseEntity.ok().body(new PointResponse(member.getPoint()));
    }

}
