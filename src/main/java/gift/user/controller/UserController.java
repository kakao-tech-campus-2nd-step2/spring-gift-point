package gift.user.controller;

import gift.user.dto.UserDto;
import gift.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "User API", description = "사용자 관련 API")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  @Operation(summary = "회원 가입")
  public ResponseEntity<?> register(
      @RequestBody @Parameter(description = "사용자 데이터", required = true) UserDto userDto) {
    String token = userService.register(userDto);
    return ResponseEntity.ok(new TokenResponse(token));
  }

  @PostMapping("/login")
  @Operation(summary = "로그인")
  public ResponseEntity<?> login(
      @RequestBody @Parameter(description = "사용자 데이터", required = true) UserDto userDto) {
    String token = userService.authenticate(userDto.getEmail(), userDto.getPassword());
    return ResponseEntity.ok(new TokenResponse(token));
  }
}

