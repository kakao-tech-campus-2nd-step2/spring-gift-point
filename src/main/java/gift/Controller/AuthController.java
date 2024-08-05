package gift.Controller;

import gift.DTO.AuthRequestDTO;
import gift.DTO.AuthResponseDTO;
import gift.DTO.TokenResponseDTO;
import gift.DTO.UserDTO;
import gift.Service.UserService;
import gift.Mapper.UserServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "유저 관련 서비스", description = "기본 로그인(카카오x) 및 회원관리")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserServiceMapper userServiceMapper;


    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @PostMapping("/user/register")
    public ResponseEntity<Void> registerUser(@RequestBody UserDTO userDTO) {
        userService.registerUser(userDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "로그인", description = "사용자 인증 후 JWT 토큰을 반환합니다.")
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenResponseDTO> loginUser(@RequestBody UserDTO userDTO) {
        String token = userService.authenticateUser(userDTO);
        return ResponseEntity.ok(new TokenResponseDTO(token));
    }

//    @Operation(summary = "회원가입", description = "새로운 계정이 회원가입")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully registered user"),
//            @ApiResponse(responseCode = "400", description = "Invalid input")
//    })
//    @PostMapping("/register")
//    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
//        UserDTO savedUser = userService.saveUser(userDTO);
//        return ResponseEntity.ok(savedUser);
//    }
//
//    @Operation(summary = "유저 로그인", description = "이메일로 로그인(카카오x)")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully authenticated"),
//            @ApiResponse(responseCode = "401", description = "Unauthorized")
//    })
//    @PostMapping("/login")
//    public ResponseEntity<AuthResponseDTO> loginUser(@RequestBody AuthRequestDTO authRequest) {
//        AuthResponseDTO authResponse = userService.loginUser(authRequest);
//        return ResponseEntity.ok(authResponse);
//    }

//    @Operation(summary = "단일 유저 조회", description = " ")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
//            @ApiResponse(responseCode = "404", description = "User not found")
//    })
//    @GetMapping("/{id}")
//    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
//        return userServiceMapper.toResponseEntity(userService.findUserById(id));
//    }
//
//    @Operation(summary = "유저 정보 수정", description = "비밀번호 및 이메일 수정")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully updated user"),
//            @ApiResponse(responseCode = "404", description = "User not found"),
//            @ApiResponse(responseCode = "400", description = "Invalid input")
//    })
//    @PutMapping("/{id}")
//    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
//        try {
//            UserDTO updatedUser = userService.updateUser(id, userDTO);
//            return ResponseEntity.ok(updatedUser);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @Operation(summary = "유저 삭제", description = " ")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "204", description = "Successfully deleted user"),
//            @ApiResponse(responseCode = "404", description = "User not found")
//    })
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//        userService.deleteUser(id);
//        return ResponseEntity.noContent().build();
//    }
}
