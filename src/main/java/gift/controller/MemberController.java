package gift.controller;

import gift.annotation.LoginUser;
import gift.dto.ErrorResponse;
import gift.dto.MemberDTO;
import gift.dto.PointDTO;
import gift.dto.PointRequestDTO;
import gift.entity.Member;
import gift.response.AuthResponse;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Member(회원)", description = "Member관련 API입니다.")
public class MemberController {

    MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "회원가입", description = "Id, Password로 회원을 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "회원가입 성공"),
        @ApiResponse(responseCode = "400", description = "입력 데이터 잘못됨.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class))))
    })
    @PostMapping("/register")
    public ResponseEntity<String> signUp(@Valid @RequestBody MemberDTO memberDTO) {
        Member member = memberDTO.toEntity();
        memberService.generateUser(member);

        return new ResponseEntity<>("회원가입 완료", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "Id, Password가 일치하면 로그인 성공 후 accessToken을 반환합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "입력 데이터 잘못됨.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
        @ApiResponse(responseCode = "403", description = "회원 정보 불일치.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class))))})

    public ResponseEntity<AuthResponse> login(@RequestBody MemberDTO memberDTO) {
        Member member = memberDTO.toEntity();
        AuthResponse response = new AuthResponse(memberService.authenticateUser(member));
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/points")
    @Operation(summary = "포인트 조회", description = "로그인한 사용자의 포인트 조회.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "포인트 조회 성공", content = @Content(schema = @Schema(implementation = PointDTO.class))),
        @ApiResponse(responseCode = "401", description = "인증 필요.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class))))})
    public ResponseEntity<PointDTO> getPoint(@LoginUser String email) {
        PointDTO point = memberService.getPoint(email);
        return new ResponseEntity<>(point, HttpStatus.OK);
    }

    @PostMapping("/{memberId}/points")
    @Operation(summary = "포인트 추가", description = "MemberId에 해당하는 멤버에게 포인트를 추가합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "입력 데이터 잘못됨.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
        @ApiResponse(responseCode = "404", description = "회원 정보 불일치.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class))))})

    public ResponseEntity<String> addPoint(@PathVariable("memberId") Long memberId,
        @RequestBody PointRequestDTO pointDTO) {
        memberService.addPoint(memberId, pointDTO.getPoint());
        return new ResponseEntity<>("Point 추가 완료", HttpStatus.OK);

    }


}
