package gift.controller;

import gift.dto.MemberRequest;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@Tag(name = "Member API", description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    @Operation(summary = "회원 가입", description = "새로운 회원을 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = {@Content(schema = @Schema(implementation = String.class))}),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일 - 회원가입 실패")
    })
    public ResponseEntity<String> register(@RequestBody MemberRequest memberRequest) {
        String token = memberService.register(memberRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        return ResponseEntity.ok().headers(headers).body("{\"token\": \"" + token + "\"}");
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "회원 로그인을 처리합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = {@Content(schema = @Schema(implementation = String.class))}),
        @ApiResponse(responseCode = "403", description = "사용자 인증 실패")
    })
    public ResponseEntity<String> login(@RequestBody MemberRequest memberRequest) {
        String token = memberService.login(memberRequest.getEmail(), memberRequest.getPassword());
        if (token != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            return ResponseEntity.ok().headers(headers).body("{\"token\": \"" + token + "\"}");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body("{\"error\": \"존재하지 않는 이메일이거나 비밀번호가 잘못되었습니다.\"}");
    }

}
