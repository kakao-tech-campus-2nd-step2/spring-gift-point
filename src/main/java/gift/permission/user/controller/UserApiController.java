package gift.permission.user.controller;

import gift.global.dto.ApiResponseDto;
import gift.permission.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApiController {

    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    // access token이 만료된 경우, front에서 갖고 있는 refresh token을 서버로 보내서 검증 후 access token 반환
    // RESTful하게 url을 위해 get요청을 사용하였는데, Param으로 refreshToken같은 탈취당하면 안되는 정보를 보내는 것이 괜찮은지 궁금합니다.
    @PostMapping("/api/login/access-token")
    public ApiResponseDto<String> getAccessToken(
        @RequestParam(name = "refresh-token") String refreshToken) {
        return ApiResponseDto.SUCCESS(userService.getAccessToken(refreshToken));
    }
}
