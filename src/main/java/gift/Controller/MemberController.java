package gift.Controller;

import gift.Model.MemberDto;
import gift.Service.MemberService;
import gift.Utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Member", description = "회원 관련 api")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @Autowired
    public MemberController(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/login")
    @Operation(summary = "로그인 페이지 전환", description = "로그인 페이지로 전환합니다.")
    public ResponseEntity<?> login() {
        try {
            ClassPathResource resource = new ClassPathResource("templates/login.html");
            String htmlContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            return new ResponseEntity<>(htmlContent, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error loading login page", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인을 수행합니다.")
    public ResponseEntity<?> login(@RequestBody MemberDto memberDto, HttpServletResponse response) {
        String email = memberDto.getEmail();
        String password = memberDto.getPassword();

        boolean isAuthenticated = memberService.authenticate(email, password);
        if (isAuthenticated) {
            boolean isAdmin = memberService.isAdmin(email);
            Optional<MemberDto> authenticatedMember = memberService.findByEmail(email);
            String token = jwtUtil.generateToken(authenticatedMember.get(), isAdmin);
            // Set token in HttpOnly cookie
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            HttpHeaders headers = new HttpHeaders();
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("token", token);
            body.add("email", email);

            if (isAdmin) {

                headers.add("Location", "/api/products");
                return new ResponseEntity<>(body, headers, HttpStatus.OK);
            }

            headers.add("Location", "/products");
            return new ResponseEntity<>(body, headers, HttpStatus.OK);
        }

        return ResponseEntity.ok("Authentication failed");
    }

    @GetMapping( "/register")
    @Operation(summary = "회원가입", description = "회원가입 페이지를 보여줍니다.")
    public ResponseEntity<String> register() {
        try {
            ClassPathResource resource = new ClassPathResource("templates/register.html");
            String htmlContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            return new ResponseEntity<>(htmlContent, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error loading login page", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("register")
    @Operation(summary = "회원가입", description = "회원가입을 수행합니다.")
    public ResponseEntity<String> register(@RequestBody MemberDto memberDto) {
        memberService.register(memberDto);
        String token = jwtUtil.generateToken(memberDto, false);// 관리자에 대한 회원가입은 보여주지 않음
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/categories");
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("token", token);
        body.add("email", memberDto.getEmail());
        return new ResponseEntity<>("Successfully registered", headers, HttpStatus.SEE_OTHER);
    }

}
