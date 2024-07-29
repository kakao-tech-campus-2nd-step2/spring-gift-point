package gift.controller;

import gift.dto.UserLoginDto;
import gift.dto.UserRegisterDto;
import gift.dto.UserResponseDto;
import gift.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "사용자 관련 API")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "회원 가입", description = "새로운 사용자를 등록합니다.")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody UserRegisterDto userRegisterDTO) {
        UserResponseDto userResponseDTO = userService.registerUser(userRegisterDTO);
        return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자가 로그인합니다.")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginDto userLoginDTO) {
        String token = userService.loginUser(userLoginDTO);
        return new ResponseEntity<>("{\"accessToken\": \"" + token + "\"}", HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "모든 사용자 조회", description = "모든 사용자를 조회합니다.")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "사용자 조회", description = "ID로 사용자를 조회합니다.")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        UserResponseDto user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "사용자 수정", description = "기존 사용자를 수정합니다.")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody UserRegisterDto userRegisterDTO) {
        UserResponseDto updatedUser = userService.updateUser(id, userRegisterDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "사용자 삭제", description = "기존 사용자를 삭제합니다.")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
