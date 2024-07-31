package gift.Controller;

import gift.DTO.JwtToken;
import gift.DTO.MemberDto;
import gift.Service.MemberService;
import jakarta.validation.Valid;
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

  @PostMapping("/signup")
  public ResponseEntity<MemberDto> SignUp(@Valid @RequestBody MemberDto memberDtoInfo) {
    MemberDto memberDto = memberService.SignUp(memberDtoInfo);
    return ResponseEntity.ok(memberDto);
  }

  @PostMapping("/login")
  public ResponseEntity<JwtToken> Login(
    @Valid @RequestBody MemberDto memberDtoInfo) {
    JwtToken jwtToken = memberService.Login(memberDtoInfo);
    return ResponseEntity.ok(jwtToken);
  }
}
