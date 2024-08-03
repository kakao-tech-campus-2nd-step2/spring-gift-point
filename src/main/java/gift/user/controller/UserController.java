package gift.user.controller;

import gift.security.LoginMember;
import gift.user.dto.PointRequestDto;
import gift.user.dto.PointResponseDto;
import gift.user.dto.TokenResponseDto;
import gift.user.dto.UserRequestDto;
import gift.user.entity.User;
import gift.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@Tag(name = "User API", description = "사용자 관련 API")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  @Operation(summary = "회원 가입", description = "새 회원을 등록하고 토큰을 받는다.")
  public ResponseEntity<TokenResponseDto> register(
      @RequestBody @Parameter(description = "사용자 데이터", required = true) @Valid UserRequestDto userRequestDto) {
    TokenResponseDto tokenResponse = userService.register(userRequestDto);
    return new ResponseEntity<>(tokenResponse, HttpStatus.CREATED);
  }

  @PostMapping("/login")
  @Operation(summary = "로그인", description = "회원을 인증하고 토큰을 받는다.")
  public ResponseEntity<TokenResponseDto> login(
      @RequestBody @Parameter(description = "사용자 데이터", required = true) @Valid UserRequestDto userRequestDto) {
    TokenResponseDto tokenResponse = userService.authenticate(userRequestDto.email(),
        userRequestDto.password());
    return ResponseEntity.ok(tokenResponse);
  }

  @GetMapping("/point")
  @Operation(summary = "포인트 조회", description = "개인의 포인트를 조회할 수 있다")
  public ResponseEntity<PointResponseDto> getPoint(@LoginMember User user) {
    PointResponseDto pointResponse = userService.getPoint(user.getId());
    return ResponseEntity.ok(pointResponse);
  }

  @PostMapping("/point")
  @Operation(summary = "포인트 관리", description = "포인트를 적립하거나 사용할 수 있다.")
  public ResponseEntity<PointResponseDto> usePoint(
      @LoginMember User user, @RequestBody @Valid PointRequestDto pointRequestDto) {
    PointResponseDto pointResponseDto = userService.managePoint(user.getId(), pointRequestDto);
    return ResponseEntity.ok(pointResponseDto);
  }

}
