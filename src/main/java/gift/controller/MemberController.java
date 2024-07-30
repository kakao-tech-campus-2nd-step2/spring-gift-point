package gift.controller;

import gift.dto.request.LoginMemberDTO;
import gift.dto.request.TokenLoginRequestDTO;
import gift.dto.request.NormalMemberRequestDTO;
import gift.entity.Member;
import gift.service.LoginMember;
import gift.service.MemberService;
import jdk.jfr.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/members")
@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/signup")
    public String signupRendering() {
        return "signup";
    }

    @GetMapping("/login")
    public String loginRendering() {
        return "login";
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody NormalMemberRequestDTO normalMemberRequestDTO) {
        String token = memberService.addMember(normalMemberRequestDTO);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", token);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body(responseBody);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody NormalMemberRequestDTO normalMemberRequestDTO) {
        String token = memberService.login(normalMemberRequestDTO);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", token);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body(responseBody);
    }

    @Description("정상적으로 token으로 인증이 되는지 test하는 method")
    @PostMapping("/token-login")
    public ResponseEntity<String> tokenLogin(@LoginMember LoginMemberDTO loginMemberDTO) {
        memberService.tokenLogin(loginMemberDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body("토큰 인증 성공");
    }

    @Description("임시 확인용 html form. service x ")
    @GetMapping("/user-info")
    public ResponseEntity<List<Member>> userInfoRendering() {
        List<Member> members = memberService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).
                body(members);
    }

}
