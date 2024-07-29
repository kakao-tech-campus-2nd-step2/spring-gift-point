package gift.Controller;

import gift.DTO.RequestMemberDTO;
import gift.Model.Entity.Member;
import gift.Service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "로그인 및 회원가입", description = "로그인 및 회원가입 API")
@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;


    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "회원가입", description = "회원가입을 진행합니다")
    @ApiResponse(responseCode = "201", description = "회원가입 완료")
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 입력값을 확인해주세요")
    @ApiResponse(responseCode = "500", description = "서버 내부 에러 발생")
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RequestMemberDTO requestMemberDTO){
        Member member = memberService.signUpUser(requestMemberDTO);
        return ResponseEntity.created(URI.create("/members" + member.getId())).body("회원가입이 정상적으로 되었습니다");
    }

    @Operation(summary = "로그인", description = "존재하는 사용자인지 확인 후 토큰을 발급합니다" )
    @ApiResponse(responseCode = "200", description = "로그인 완료")
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 입력값을 확인해주세요")
    @ApiResponse(responseCode = "500", description = "서버 내부 에러 발생")
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody RequestMemberDTO member) {
        return ResponseEntity.ok(memberService.loginUser(member));
    }
}
