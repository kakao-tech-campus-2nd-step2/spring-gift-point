package gift.domain.member.controller;

import gift.domain.member.dto.MemberRequest;
import gift.domain.member.dto.MemberResponse;
import gift.domain.member.service.MemberService;
import gift.util.dto.JwtResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@Tag(name = "MemberController", description = "Member API")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping()
    @Operation(summary = "전체 유저 조회", description = "전체 유저를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @Parameters({
        @Parameter(name = "pageNo", description = "페이지 번호 (0부터 시작)", example = "0"),
        @Parameter(name = "pageSize", description = "페이지 크기", example = "10")
    })
    public ResponseEntity<Page<MemberResponse>> getAllMember(
        @RequestParam(defaultValue = "0") int pageNo,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        Page<MemberResponse> responses = memberService.getAllMember(pageNo, pageSize);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/register")
    @Operation(summary = "회원 가입", description = "회원 가입을 요청합니다.")
    @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    public ResponseEntity<JwtResponse> register(@RequestBody MemberRequest memberRequest) {
        String token = memberService.register(memberRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new JwtResponse(token));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인을 진행합니다.")
    @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    public ResponseEntity<JwtResponse> login(@RequestBody MemberRequest memberRequest) {

        String token = memberService.login(memberRequest);

        if (token != null) {
            return ResponseEntity.ok(new JwtResponse(token));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "회원 삭제", description = "저장된 회원을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "요청에 성공하였습니다.")
    @Parameter(name = "id", description = "삭제할 회원의 Id")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}