package gift.controller;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.service.JwtProvider;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    public MemberController(MemberService memberService, JwtProvider jwtProvider) {
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
    }

    @Operation(summary = "새로운 회원을 등록")
    @PostMapping("/register")
    public ResponseEntity<MemberResponseDto> registerMember(@RequestBody MemberRequestDto requestDto){
        memberService.save(requestDto);
        String token = jwtProvider.createToken(requestDto.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(new MemberResponseDto(requestDto.getEmail(),token));
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<MemberResponseDto> login(@RequestBody MemberRequestDto requestDto){
        // 회원 존재 확인 : 여기서 없으면 MemberNotException을 던지는데, 발생하는 에러를 여기서 잡지않고 GlobalExceptionHandler에서 잡는다.
        memberService.authenticate(requestDto.getEmail(),requestDto.getPassword());
        // Access Token 토큰 생성
        String token = jwtProvider.createToken(requestDto.getEmail());
        // 응답 생성
        //HashMap<String,String> response = new HashMap<>();
        //response.put("token",token);

        return ResponseEntity.status(HttpStatus.OK).body(new MemberResponseDto(requestDto.getEmail(),token));
    }
}
