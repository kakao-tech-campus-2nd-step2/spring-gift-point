package gift.users.kakao;

import gift.error.KakaoOrderException;
import gift.response.ApiResponse;
import gift.response.ApiResponse.HttpResult;
import gift.users.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "order API", description = "order related API")
public class KakaoOrderApiController {

    private final KakaoOrderService kakaoOrderService;
    private final UserService userService;

    public KakaoOrderApiController(KakaoOrderService kakaoOrderService, UserService userService) {
        this.kakaoOrderService = kakaoOrderService;
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "kakao ordering", description = "카카오 아이디로 가입된 회원의 아이디로 주문을 하고, 카카오 메시지를 보냅니다.")
    public ResponseEntity<ApiResponse<KakaoOrderDTO>> kakaoOrder(
        @Valid @RequestBody KakaoOrderDTO kakaoOrderDTO,
        HttpServletRequest request) {

        LocalDateTime currentDateTime = LocalDateTime.now();
        String orderDateTime = currentDateTime.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        Long userId = (Long) request.getAttribute("userId");
        if (!userService.findSns(userId).equals("kakao")) {
            throw new KakaoOrderException("카카오 유저만 이용할 수 있는 서비스입니다.");
        }

        KakaoOrderDTO result = kakaoOrderService.kakaoOrder(userId, kakaoOrderDTO,
            orderDateTime);
        ApiResponse<KakaoOrderDTO> apiResponse = new ApiResponse<>(HttpResult.OK,
            "카카오 주문 메시지 보내기 성공", HttpStatus.OK, result);
        return ResponseEntity.ok(apiResponse);
    }
}
