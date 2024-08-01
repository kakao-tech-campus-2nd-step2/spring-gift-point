package gift.member.controller;

import gift.exception.IllegalEmailException;
import gift.member.dto.MemberRequest;
import gift.member.dto.MemberResponse;
import gift.member.service.MemberService;
import gift.model.HttpResult;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<MemberResponse> register(@RequestBody MemberRequest memberRequest)
        throws IllegalEmailException {
        Map<String, Object> memberStringObjectMap = new LinkedHashMap<>();
        return memberService.registerMember(memberRequest)
            .map(token -> { // Optional<String>을 mapping -> isPresent면 map 안 실행 // 매개변수 token으로
                putMemberFields(memberRequest, memberStringObjectMap, token);
                return ResponseEntity.ok(
                    new MemberResponse(HttpResult.OK, "회원 가입 성공", HttpStatus.OK,
                        Collections.singletonList(memberStringObjectMap)));
            }).orElseGet(() -> { // isEmpty
                memberStringObjectMap.put("error", "비밀번호를 다시 입력해 주십시오.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new MemberResponse(HttpResult.ERROR, "회원 가입 실패", HttpStatus.BAD_REQUEST,
                        Collections.singletonList(memberStringObjectMap)));
            });
    }

    private static void putMemberFields(MemberRequest member,
        Map<String, Object> memberStringObjectMap, String token) {
        memberStringObjectMap.put("email", member.getEmail());
        memberStringObjectMap.put("password", member.getPassword());
        memberStringObjectMap.put("token", token);
    }


    @PostMapping("/login")
    public ResponseEntity<MemberResponse> login(@RequestBody MemberRequest member)
        throws IllegalEmailException {
        Map<String, Object> memberStringObjectMap = new LinkedHashMap<>();
        return memberService.login(member.getEmail(), member.getPassword())
            .map(token -> { // 토큰이 리턴 -> 정상 로그인 됨
                putMemberFields(member, memberStringObjectMap, token);
                return ResponseEntity.ok(new MemberResponse(HttpResult.OK, "로그인 성공", HttpStatus.OK,
                    Collections.singletonList(memberStringObjectMap)));
            }).orElseGet(() -> {
                memberStringObjectMap.put("Error", "존재 하지 않는 계정입니다.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    new MemberResponse(HttpResult.ERROR, "로그인 실패", HttpStatus.FORBIDDEN,
                        Collections.singletonList(memberStringObjectMap)));
            });
    }


}
