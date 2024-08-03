package gift.user.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import gift.user.domain.Role;
import gift.dto.request.UserDetails;
import gift.user.service.OAuthservice;
import gift.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "User", description = "유저 관련 정적 주소 모음")
@Controller
public class UserController {

	private final OAuthservice oAuthService;
	private final UserService userService;

	@Value("${kakao.front_token_url}")
	private String frontUrl;
	@Value("${kakao.redirectUri}")
	private String redirectUri;

	public UserController(OAuthservice oAuthService, UserService userService) {
		this.oAuthService = oAuthService;
		this.userService = userService;
	}

	@Operation(summary = "로그인 페이지", description = "로그인 페이지로 이동")
	@GetMapping("/oauth/kakao")
	public String login() {
		return "redirect:https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=49ff9872833e378d8e9194a03d3ad636&redirect_uri="+redirectUri;
	}

	@Operation(summary = "카카오 로그인", description = "카카오 로그인 페이지 완료후 토큰과 함께 프런트로 리다이렉트하기")
	@GetMapping("/oauth/kakao/redirect")
	public RedirectView login(@RequestParam("code") String code,
		HttpServletResponse response) {

		String email = oAuthService.authenticate(code);
		String token = userService.loginOauth2User(email);

		//
		return new RedirectView(frontUrl + "?tokenValue=" + token);
	}
}
