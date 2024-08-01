package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.response.CommonResponse;
import gift.dto.response.LoginResponse;
import gift.dto.response.PointsResponse;
import gift.dto.response.RegisterResponse;
import gift.dto.request.MemberRequest;
import gift.service.JwtUtil;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/members")
public class MemberRestController {
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public MemberRestController(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "새로운 회원을 등록합니다")
    @PostMapping("/register")
    public ResponseEntity<CommonResponse> registerMember(@RequestBody MemberRequest request){
        memberService.save(request);

        String token = jwtUtil.generateToken(request.email());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(new CommonResponse<>(new RegisterResponse(request.email(), token),"회원 가입 성공", true));
    }

    @Operation(summary = "로그인하고 액세스 토큰(jwt 토큰)을 반환합니다")
    @PostMapping("/login")
    public ResponseEntity<CommonResponse> giveAccessToken(@RequestBody MemberRequest request) {
        memberService.checkMemberExistsByEmailAndPassword(request.email(),request.password());

        String token = jwtUtil.generateToken(request.email());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(new CommonResponse<>(new LoginResponse(request.email(), token), "로그인 성공", true));
    }

    @Operation(summary = "회원 포인트를 조회합니다.")
    @GetMapping("/points")
    public ResponseEntity<CommonResponse> getMemberPoints(@LoginMember MemberRequest memberRequest){
        PointsResponse pointsResponse = new PointsResponse(memberService.getMemberPoints(memberRequest));
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse<>(pointsResponse,"회원 포인트 조회 성공", true));
    }
}
