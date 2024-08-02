package gift.user.controller;

import gift.user.dto.TokenResponseDto;
import gift.user.dto.UserRequestDto;
import gift.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
      @RequestBody @Parameter(description = "사용자 데이터", required = true) UserRequestDto userRequestDto) {
    TokenResponseDto tokenResponse = userService.register(userRequestDto);
    return new ResponseEntity<>(tokenResponse, HttpStatus.CREATED);
  }

  @PostMapping("/login")
  @Operation(summary = "로그인", description = "회원을 인증하고 토큰을 받는다.")
  public ResponseEntity<TokenResponseDto> login(
      @RequestBody @Parameter(description = "사용자 데이터", required = true) UserRequestDto userRequestDto) {
    TokenResponseDto tokenResponse = userService.authenticate(userRequestDto.email(), userRequestDto.password());
    return ResponseEntity.ok(tokenResponse);
  }
}
