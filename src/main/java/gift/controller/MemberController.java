package gift.controller;

import gift.model.Member;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "MemberController", description = "회원 관련 API")
@RestController
@RequestMapping("/members")
public class MemberController {

  private final MemberService memberService;

  public MemberController(MemberService memberService) {
    this.memberService = memberService;
  }

  @Operation(summary = "회원 등록", description = "새로운 회원을 등록합니다.")
  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody Member member) {
    String token = memberService.register(member);
    return ResponseEntity.ok(token);
  }

  @Operation(summary = "회원 로그인", description = "회원 로그인을 처리합니다.")
  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody Member member) {
    String token = memberService.login(member.getEmail(), member.getPassword());
    return ResponseEntity.ok(token);
  }
}