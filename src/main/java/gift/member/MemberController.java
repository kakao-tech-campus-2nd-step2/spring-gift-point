package gift.member;

import gift.member.dto.MemberChargePointRequestDTO;
import gift.member.dto.MemberPointResponseDTO;
import gift.member.dto.MemberRequestDTO;
import gift.token.TokenResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member", description = "Member API")
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "회원 가입", description = "사용자를 등록하고, 액세스 토큰을 발급합니다.")
    @ApiResponse(responseCode = "201", description = "정상")
    @ApiResponse(responseCode = "400", description = "중복된 이메일이 존재하는 경우")
    @ApiResponse(responseCode = "400", description = "입력 양식이 잘못된 경우")
    @ApiResponse(responseCode = "500", description = "서버 에러")
    public TokenResponseDTO register(@RequestBody MemberRequestDTO memberRequestDTO) {
        return new TokenResponseDTO(memberService.register(memberRequestDTO));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 정보를 확인하고, 액세스 토큰을 발급합니다.")
    @ApiResponse(responseCode = "200", description = "정상")
    @ApiResponse(responseCode = "400", description = "잘못된 입력 양식입니다.")
    @ApiResponse(responseCode = "403", description = "인증 실패")
    @ApiResponse(responseCode = "500", description = "서버 에러")
    public TokenResponseDTO login(@RequestBody MemberRequestDTO memberRequestDTO) {
        return new TokenResponseDTO(memberService.login(memberRequestDTO));
    }

    @PutMapping("/charge")
    @Operation(hidden = true)
    public void chargePoint(@RequestBody MemberChargePointRequestDTO memberPointChargeRequestDTO) {
        memberService.chargePoint(memberPointChargeRequestDTO);
    }

    @GetMapping("/point")
    @Operation(summary = "포인트 정보 가져오기", description = "사용자의 포인트를 가져온다.")
    @ApiResponse(responseCode = "200", description = "정상")
    @ApiResponse(responseCode = "403", description = "인증 실패")
    @ApiResponse(responseCode = "500", description = "서버 에러")
    public MemberPointResponseDTO getPoint(@RequestHeader("Authorization") String token) {
        return memberService.getPoint(token);
    }
}
