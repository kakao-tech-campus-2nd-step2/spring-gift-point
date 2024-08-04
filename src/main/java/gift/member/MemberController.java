package gift.member;

import gift.jwt.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "member", description = "유저 관련 API")
@Controller
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtService jwtService;

    public MemberController(MemberService memberService, JwtService jwtService) {
        this.memberService = memberService;
        this.jwtService = jwtService;
    }

    /*@Operation(summary = "회원가입 페이지", description = "작동하지 않습니다.")
    @GetMapping("/page")
    public String signup(Model model) {
        model.addAttribute("newSiteUser", new Member());
        return "signup";
    }*/

    @Operation(summary = "회원가입", description = "회원가입 합니다.")
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> createUser(@RequestBody SighUpRequest newMember) {
        memberService.createMember(newMember.getEmail(), newMember.getEmail(),
            newMember.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(""));
    }

    @Operation(summary = "로그인", description = "로그인하고 토큰 반환")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest user) {
        Member member = memberService.findByEmailAndPassword(user.getEmail(), user.getPassword());
        String jwt = jwtService.createJwt(member.getId());

        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(jwt));
    }

}
