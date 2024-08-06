package gift.Controller;

import gift.DTO.JwtToken;
import gift.DTO.MemberDto;
import gift.Service.MemberService;
import jakarta.validation.Valid;
import java.net.URI;
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

  @PostMapping("/register")
  public ResponseEntity<JwtToken> SignUp(@Valid @RequestBody MemberDto memberDtoInfo) {
    JwtToken jwtToken = memberService.SignUp(memberDtoInfo);
    return ResponseEntity.created(URI.create("/register")).body(jwtToken);
  }

  @PostMapping("/login")
  public ResponseEntity<JwtToken> Login(@Valid @RequestBody MemberDto memberDtoInfo) {
    JwtToken jwtToken = memberService.Login(memberDtoInfo);
    return ResponseEntity.ok(jwtToken);
  }
}
