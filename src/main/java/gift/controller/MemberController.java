package gift.controller;

import gift.dto.MemberPointChargingRequestDto;
import gift.dto.MemberPointViewResponseDto;
import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.service.BasicTokenService;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import java.util.List;

@Tag(name = "member", description = "회원 API")
@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;
    private final BasicTokenService basicTokenService;

    public MemberController(MemberService memberService, BasicTokenService basicTokenService) {
        this.memberService = memberService;
        this.basicTokenService = basicTokenService;
    }

    @GetMapping("")
    @Operation(summary = "인증 메인화면", description = "인증과정의 메인 화면으로 이동합니다.")
    public String main() {
        return "auth/index";
    }

    @GetMapping("/login")
    @Operation(summary = "로그인 화면", description = "로그인 화면으로 이동합니다.")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/sign-up")
    @Operation(summary = "회원가입 화면", description = "회원가입 화면으로 이동합니다.")
    public String signUp() {
        return "auth/sign-up";
    }

    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "회원정보 값(id,email)을 입력하면 회원정보를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원정보 저장 성공", content = @Content(schema = @Schema(implementation = MemberResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<MemberResponseDto> save(@ModelAttribute MemberRequestDto memberRequestDto) {
        MemberResponseDto memberResponseDto = memberService.save(memberRequestDto.getEmail(), memberRequestDto.getPassword());
        return new ResponseEntity<>(memberResponseDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "(id,email)을 입력하면 로그인 로직을 수행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = MemberResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "로그인 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<MemberResponseDto> login(@ModelAttribute MemberRequestDto memberRequestDto) throws AuthenticationException {
        if (memberService.login(memberRequestDto.getEmail(), memberRequestDto.getPassword())) {
            return new ResponseEntity<>(new MemberResponseDto(memberRequestDto.getEmail(), memberService.generateTokenFrom(memberRequestDto.getEmail())), HttpStatus.OK);
        }
        throw new AuthenticationException("로그인 실패");
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "멤버목록 조회 성공", content = @Content(schema = @Schema(implementation = MemberResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/list")
    @Operation(summary = "멤버 목록 조회", description = "멤버 목록을 조회합니다.")
    public ResponseEntity<List<MemberResponseDto>> MemberList() {
        List<MemberResponseDto> memberResponseDto = memberService.getAllMemberResponseDto();
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "멤버 포인트 충전 성공", content = @Content(schema = @Schema(implementation = MemberResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/point")
    @Operation(summary = "멤버 포인트 충전", description = "멤버가 가진 포인트를 충전합니다.")
    public ResponseEntity<MemberPointViewResponseDto> ChargeMemberPoint(
            @RequestBody MemberPointChargingRequestDto memberPointChargingRequestDto,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        Long memberId = basicTokenService.getUserIdByDecodeTokenValue(authorizationHeader);
        MemberPointViewResponseDto remainPoint = memberService.updateMemberPoint(memberPointChargingRequestDto, memberId);
        return new ResponseEntity<>(remainPoint, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "멤버 포인트 조회 성공", content = @Content(schema = @Schema(implementation = MemberResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/point")
    @Operation(summary = "멤버 포인트 조회", description = "멤버 포인트를 조회합니다.")
    public ResponseEntity<MemberPointViewResponseDto> ViewMemberPoint(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        Long memberId = basicTokenService.getUserIdByDecodeTokenValue(authorizationHeader);
        MemberPointViewResponseDto remainPoint = memberService.getMemberPoint(memberId);
        return new ResponseEntity<>(remainPoint, HttpStatus.OK);
    }
}
