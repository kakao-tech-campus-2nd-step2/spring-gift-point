package gift.controller;

import gift.dto.UserLoginDto;
import gift.dto.UserRegisterDto;
import gift.dto.UserResponseDto;
import gift.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Users", description = "사용자 관련 API")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "회원 가입", description = "새로운 사용자를 등록합니다.",
            requestBody = @RequestBody(
                    description = "등록할 사용자의 정보",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UserRegisterDto.class)
                    )
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "사용자가 성공적으로 등록되었습니다.",
                    content = @Content(
                            schema = @Schema(implementation = UserResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청. 예: 이메일이 이미 존재하는 경우.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody UserRegisterDto userRegisterDTO) {
        UserResponseDto userResponseDTO = userService.registerUser(userRegisterDTO);
        return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자가 로그인합니다.",
            requestBody = @RequestBody(
                    description = "로그인할 사용자의 정보",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UserLoginDto.class)
                    )
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인이 성공적으로 완료되었습니다.",
                    content = @Content(
                            schema = @Schema(example = "{\"accessToken\": \"string\"}")
                    )),
            @ApiResponse(responseCode = "403", description = "이메일 또는 비밀번호가 잘못되었습니다.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<String> loginUser(@RequestBody UserLoginDto userLoginDTO) {
        String token = userService.loginUser(userLoginDTO);
        return new ResponseEntity<>("{\"accessToken\": \"" + token + "\"}", HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "모든 사용자 조회", description = "모든 사용자를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 목록이 성공적으로 반환되었습니다.",
                    content = @Content(
                            schema = @Schema(implementation = UserResponseDto.class)
                    )),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "사용자 조회", description = "ID로 사용자를 조회합니다.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "id",
                            description = "조회할 사용자 ID",
                            required = true,
                            schema = @Schema(type = "integer")
                    )
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자가 성공적으로 반환되었습니다.",
                    content = @Content(
                            schema = @Schema(implementation = UserResponseDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        UserResponseDto user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "사용자 수정", description = "기존 사용자를 수정합니다.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "id",
                            description = "수정할 사용자 ID",
                            required = true,
                            schema = @Schema(type = "integer")
                    )
            },
            requestBody = @RequestBody(
                    description = "수정할 사용자의 정보",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UserRegisterDto.class)
                    )
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자가 성공적으로 수정되었습니다.",
                    content = @Content(
                            schema = @Schema(implementation = UserResponseDto.class)
                    )),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody UserRegisterDto userRegisterDTO) {
        UserResponseDto updatedUser = userService.updateUser(id, userRegisterDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "사용자 삭제", description = "기존 사용자를 삭제합니다.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "id",
                            description = "삭제할 사용자 ID",
                            required = true,
                            schema = @Schema(type = "integer")
                    )
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "사용자가 성공적으로 삭제되었습니다."),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
