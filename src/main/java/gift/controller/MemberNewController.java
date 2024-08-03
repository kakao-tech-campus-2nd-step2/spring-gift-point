package gift.controller;

import gift.dto.MemberDto;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@Tag(name = "[협업] Member API", description = "[협업]회원 컨트롤러")
public class MemberNewController {

    private final MemberService memberService;

    @Autowired
    public MemberNewController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인을 처리합니다.")
    public ResponseEntity<?> login(@RequestBody MemberDto memberDto) {
        try {
            String token = memberService.login(memberDto.getEmail(), memberDto.getPassword());
            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {
            // HTTP 상태 코드 401 (Unauthorized)와 함께 에러 메시지 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "회원가입을 처리합니다.")
    public ResponseEntity<?> register(@RequestBody MemberDto memberDto) {
        String token = memberService.registerMember(memberDto);
        return ResponseEntity.ok(token);
    }

}
