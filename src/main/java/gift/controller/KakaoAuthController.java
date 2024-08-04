package gift.controller;

import gift.dto.ApiResponse;
import gift.dto.UserInfo;
import gift.service.KakaoAuthService;
import gift.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;
    private final UserService userService;

    public KakaoAuthController(KakaoAuthService kakaoAuthService, UserService userService) {
        this.kakaoAuthService = kakaoAuthService;
        this.userService = userService;
    }

    @GetMapping("/oauth/kakao")
    public ApiResponse<String> kakaoLogin(@RequestParam("code") String authorizationCode, HttpServletRequest request) {
        try {
            String accessToken = kakaoAuthService.getAccessToken(authorizationCode);
            UserInfo userInfo = kakaoAuthService.getUserInfo(accessToken);

            HttpSession session = request.getSession();
            session.setAttribute("accessToken", accessToken);
            session.setAttribute("userInfo", userInfo);

            userService.saveUser(accessToken, userInfo);

            return new ApiResponse<>(true, "Access token retrieved successfully", accessToken, null);
        } catch (Exception e) {
            return new ApiResponse<>(true, "Access token retrieved successfully", null, null);
        }
    }
}
