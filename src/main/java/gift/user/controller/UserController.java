package gift.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import gift.user.domain.Role;
import gift.dto.request.UserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User", description = "유저 관련 정적 주소 모음")
@Controller
public class UserController {

	@Operation(summary = "로그인 페이지", description = "로그인 페이지로 이동")
	@GetMapping("/oauth/kakao")
	public String login() {
		return "redirect:https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=49ff9872833e378d8e9194a03d3ad636&redirect_uri=http://localhost:8080/oauth/kakao/redirect";
	}
}
