package gift.Controller;

import gift.DTO.MemberDTO;
import gift.DTO.PointDTO;
import gift.DTO.RemainingPointsDTO;
import gift.DTO.SuccessMessageDTO;
import gift.DTO.TokenDTO;
import gift.Service.MemberAccessTokenProvider;
import gift.Service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member", description = "Member 관련 API")
@RestController
public class MemberController {
    private final MemberService memberService;
    private final MemberAccessTokenProvider memberAccessTokenProvider;

    public MemberController(MemberService memberService, MemberAccessTokenProvider memberAccessTokenProvider){
        this.memberService = memberService;
        this.memberAccessTokenProvider = memberAccessTokenProvider;
    }
    @Operation(
        summary = "회원 가입",
        description = "전달된 정보로 회원가입"
    )
    @ApiResponse(
        responseCode = "201",
        description = "회원가입 성공"
    )
    @Parameter(name = "memberDTO", description = "회원 정보")
    @PostMapping("/api/members/register")
    public ResponseEntity<SuccessMessageDTO> signupMember(@Valid @RequestBody MemberDTO memberDTO){
        memberService.signupMember(memberDTO);
        SuccessMessageDTO successMessageDTO = new SuccessMessageDTO("User registered successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(successMessageDTO);
    }
    @Operation(
        summary = "회원 로그인",
        description = "전달된 정보로 로그인"
    )
    @ApiResponse(
        responseCode = "200",
        description = "로그인 성공"
    )
    @Parameter(name = "memberDTO", description = "회원 정보")
    @PostMapping("/api/members/login")
    public ResponseEntity<TokenDTO> loginMember(@Valid @RequestBody MemberDTO memberDTO){
        memberService.checkMember(memberDTO);
        String token = memberAccessTokenProvider.createJwt(memberDTO.getEmail());
        TokenDTO tokenResponse = new TokenDTO(token);
        return ResponseEntity.ok().body(tokenResponse);
    }

    @Operation(
        summary = "회원 포인트 조회",
        description = "전달된 토큰으로 얻은 회원정보로 포인트 조회"
    )
    @ApiResponse(
        responseCode = "200",
        description = "포인트 조회 성공"
    )
    @Parameter(name = "request", description = "메소드 실행 전 토큰을 전달 받기 위한 객체")
    @GetMapping("/api/members/point")
    public ResponseEntity<PointDTO> getMemberPoint(HttpServletRequest request){
        String email = (String) request.getAttribute("email");
        memberService.checkMemberByEmail(email);
        return ResponseEntity.ok().body(memberService.getPoint(email));
    }

    @Operation(
        summary = "회원 포인트 사용",
        description = "전달된 정보로 로그인"
    )
    @ApiResponse(
        responseCode = "200",
        description = "포인트 차감 성공"
    )
    @Parameters({
        @Parameter(name = "request", description = "메소드 실행 전 토큰을 전달 받기 위한 객체"),
        @Parameter(name = "point", description = "차감할 point")
    })
    @PostMapping("/api/members/point")
    public ResponseEntity<RemainingPointsDTO> useMemberPoint(HttpServletRequest request, @RequestBody PointDTO pointDTO){
        String email = (String) request.getAttribute("email");
        memberService.checkMemberByEmail(email);
        return ResponseEntity.ok().body(memberService.usePoint(email,pointDTO.getPoint()));
    }

}
