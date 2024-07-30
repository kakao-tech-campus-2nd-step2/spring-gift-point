package gift.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gift.entity.User;
import gift.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "회원가입 및 로그인", description = "회원가입 및 로그인 관련 API")
@RestController
@RequestMapping("/members")
public class AuthController {
	
	private final AuthService authService;
	
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@Operation(summary = "회원가입", description = "사용자를 등록합니다.")
	@ApiResponse(responseCode = "201", description = "회원가입 성공")
	@PostMapping("/register")
	public ResponseEntity<Void> register(@Valid @RequestBody User user, BindingResult bindingResult){
		authService.createUser(user, bindingResult);
        return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@Operation(summary = "로그인", description = "사용자 로그인 후 토큰을 반환합니다.")
	@ApiResponse(responseCode = "200", description = "로그인 성공")
	@PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody User user, BindingResult bindingResult){
		Map<String, String> accessToken = authService.loginUser(user, bindingResult);
        return ResponseEntity.status(HttpStatus.OK).body(accessToken);
    }
}
