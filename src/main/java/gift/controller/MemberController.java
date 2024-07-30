package gift.controller;

import gift.dto.MemberDTO;
import gift.entity.Member;
import gift.response.AuthResponse;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Member(회원)", description = "Member관련 API입니다.")
public class MemberController {

    MemberService userService;

    public MemberController(MemberService userService) {
        this.userService = userService;
    }

    @Operation(summary = "회원가입", description = "Id, Password로 회원을 생성합니다.")
    @PostMapping("/register")
    public ResponseEntity<String> signUp(@Valid @RequestBody MemberDTO memberDTO) {
        Member member = memberDTO.toEntity();
        userService.generateUser(member);

        return new ResponseEntity<>("User 생성 완료", HttpStatus.OK);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "Id, Password가 일치하면 로그인 성공 후 accessToken을 반환합니다.")
    public ResponseEntity<AuthResponse> login(@RequestBody MemberDTO memberDTO) {
        Member member = memberDTO.toEntity();
        AuthResponse response = new AuthResponse(userService.authenticateUser(member));
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


}
