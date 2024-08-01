package gift.controller;

import gift.dto.request.LoginMemberDTO;
import gift.dto.request.LoginRequestDTO;
import gift.dto.request.RegisterRequestDTO;
import gift.dto.response.LoginResponseDTO;
import gift.dto.response.RegisterResponseDTO;
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

@RequestMapping("/api")
@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/register-page")
    public String signupRendering() {
        return "register-page";
    }

    @GetMapping("/login-page")
    public String loginRendering() {
        return "login-page";
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        System.out.println("registerRequestDTO: " + registerRequestDTO.email());
        RegisterResponseDTO registerResponseDTO = memberService.addMember(registerRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).
                body(registerResponseDTO);
    }

    @PostMapping("/register-form")
    public ResponseEntity<RegisterResponseDTO> registerByForm(@ModelAttribute RegisterRequestDTO registerRequestDTO) {
        System.out.println("registerRequestDTO: " + registerRequestDTO.email());
        RegisterResponseDTO registerResponseDTO = memberService.addMember(registerRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).
                body(registerResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO loginResponseDTO = memberService.login(loginRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).
                body(loginResponseDTO);
    }


    @PostMapping("/login-form")
    public ResponseEntity<LoginResponseDTO> loginByForm(@ModelAttribute LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO loginResponseDTO = memberService.login(loginRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).
                body(loginResponseDTO);
    }


    /*@PostMapping("/v1/register")
    public ResponseEntity<Map<String, String>> v1register(@RequestBody NormalMemberRequestDTO normalMemberRequestDTO) {
        String token = memberService.addMember(normalMemberRequestDTO);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", token);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body(responseBody);
    }

    @PostMapping("/v2/register")
    public ResponseEntity<Map<String, String>> normalregister(@RequestBody NormalMemberRequestDTO normalMemberRequestDTO) {
        String token = memberService.addMember(normalMemberRequestDTO);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", token);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body(responseBody);
    }*/


    /*@PostMapping("/v1/login")
    public ResponseEntity<RegisterResponseDTO>v1login(@RequestBody NormalMemberRequestDTO normalMemberRequestDTO) {
        RegisterResponseDTO registerResponseDTO = memberService.login(normalMemberRequestDTO);
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
    }*/

}
