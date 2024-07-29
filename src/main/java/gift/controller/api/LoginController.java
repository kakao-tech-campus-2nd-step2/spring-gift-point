package gift.controller.api;

import gift.jwt.JwtTokenProvider;
import gift.jwt.JwtUserDetailsService;
import gift.user.UserCreateForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/auth")
@Tag(name = "Login API", description = "로그인 관련 API")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUserDetailsService jwtUserDetailsService;

    @PostMapping("/login")
    @ResponseBody
    @Operation(summary = "로그인", description = "사용자 로그인 처리")
    public Map<String, String> login(@RequestBody UserCreateForm userCreateForm) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userCreateForm.getUsername(), userCreateForm.getPassword()));
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userCreateForm.getUsername());
            String token = jwtTokenProvider.createToken(userDetails);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return response;
        } catch (AuthenticationException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Invalid login credentials");
            return response;
        }
    }
}
