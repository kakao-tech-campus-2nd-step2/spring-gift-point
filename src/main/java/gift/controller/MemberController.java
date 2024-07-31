package gift.controller;

import gift.dto.ErrorResponse;
import gift.dto.MemberDTO;
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
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "회원가입 성공"),
        @ApiResponse(responseCode = "400", description = "입력 데이터 잘못됨.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class))))
    })
    @PostMapping("/register")
    public ResponseEntity<String> signUp(@Valid @RequestBody MemberDTO memberDTO) {
        Member member = memberDTO.toEntity();
        userService.generateUser(member);

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
        AuthResponse response = new AuthResponse(userService.authenticateUser(member));
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


}
