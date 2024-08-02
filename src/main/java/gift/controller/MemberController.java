package gift.controller;

import gift.dto.request.LoginMemberDTO;
import gift.dto.request.LoginRequestDTO;
import gift.dto.request.RegisterRequestDTO;
import gift.dto.response.LoginResponseDTO;
import gift.dto.response.MemberPointResponseDTO;
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

@RequestMapping("/api/members")
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

    @GetMapping("/point")
    public ResponseEntity<MemberPointResponseDTO> getPoint(@LoginMember LoginMemberDTO loginMemberDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberService.getPoint(loginMemberDTO));
    }


}
