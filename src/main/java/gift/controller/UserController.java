package gift.controller;

import gift.config.LoginUser;
import gift.dto.UserPointResponse;
import gift.dto.UserRequest;
import gift.dto.UserResponse;
import gift.entity.User;
import gift.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/members/login")
    @Operation(summary = "사용자 로그인", description = "사용자가 로그인을 시도합니다.",
        responses = @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))))
    public ResponseEntity<UserResponse> login(@RequestBody UserRequest userRequest) {
        String token = userService.authenticateUser(userRequest.getEmail(),
            userRequest.getPassword());
        UserResponse response = new UserResponse(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/members/register")
    @Operation(summary = "사용자 등록", description = "새로운 사용자를 등록합니다.",
        responses = @ApiResponse(responseCode = "200", description = "등록 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))))
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest userRequest) {
        userService.registerUser(userRequest.getEmail(), userRequest.getPassword());
        String token = userService.authenticateUser(userRequest.getEmail(),
            userRequest.getPassword());
        UserResponse response = new UserResponse(token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/points")
    @Operation(summary = "사용자 포인트 조회", description = "사용자의 잔여 포인트를 조회합니다.",
        responses = @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserPointResponse.class))))
    public ResponseEntity<UserPointResponse> getUserPoint(
        @Parameter(hidden = true) @LoginUser User user) {
        UserPointResponse userPointResponse = new UserPointResponse(userService.getUserPoint(user));
        return ResponseEntity.ok().body(userPointResponse);
    }

    @PostMapping("/points/{point}")
    @Operation(summary = "사용자 포인트 추가", description = "사용자의 포인트를 추가합니다",
        responses = @ApiResponse(responseCode = "200", description = "추가 성공", content = @Content(mediaType = "application/json")))
    public ResponseEntity<Void> addUserPoint(@LoginUser User user, @PathVariable Integer point) {
        userService.addPoint(user, point);
        return ResponseEntity.ok().build();
    }

}
