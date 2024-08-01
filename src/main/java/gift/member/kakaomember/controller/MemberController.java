package gift.controller;

import gift.dto.ApiResponse;
import gift.exception.ForbiddenException;
import gift.model.HttpResult;
import gift.model.Member;
import gift.service.MemberService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody Member member) {
        return memberService.registerMember(member)
            .map(token -> { // Optional<String>을 mapping -> isPresent면 map 안 실행 // 매개변수 token으로
                var memberRegisterSuccessResponse = new ApiResponse(HttpResult.OK,
                    "Member Register success", HttpStatus.OK);
                return ResponseEntity.ok(memberRegisterSuccessResponse);
            }).orElseGet(() -> { // isEmpty
                var memberRegisterFailResponse = new ApiResponse(HttpResult.ERROR,
                    "Registration Failed, 올바른 이메일 형식이 아닙니다.", HttpStatus.BAD_REQUEST);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(memberRegisterFailResponse);
            });
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody Member member) {
        return memberService.login(member.getEmail(), member.getPassword())
            .map(token -> { // 토큰이 리턴 -> 정상 로그인 됨
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                var memberLoginSucessResponse = new ApiResponse(HttpResult.OK,
                    "Request Success. 정상 로그인 되었습니다", HttpStatus.OK);
                return ResponseEntity.ok(memberLoginSucessResponse);
            }).orElseGet(()->{
                var apiResponse = new ApiResponse(HttpResult.ERROR, "로그인 실패",
                    HttpStatus.BAD_REQUEST);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponse);
            });
    }

}
