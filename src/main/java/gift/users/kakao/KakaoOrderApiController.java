package gift.users.kakao;

import gift.error.KakaoOrderException;
import gift.users.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PostMapping("/{userId}")
    @Operation(summary = "kakao ordering", description = "카카오 아이디로 가입된 회원의 아이디로 주문을 하고, 카카오 메시지를 보냅니다.")
    public ResponseEntity<KakaoOrderDTO> kakaoOrder(@PathVariable("userId") long userId,
        @Valid @RequestBody KakaoOrderDTO kakaoOrderDTO){

        LocalDateTime currentDateTime = LocalDateTime.now();
        String orderDateTime = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        if(!userService.findSns(userId).equals("kakao")){
            throw new KakaoOrderException("카카오 유저만 이용할 수 있는 서비스입니다.");
        }

        KakaoOrderDTO kakaoOrderResponse = kakaoOrderService.kakaoOrder(userId, kakaoOrderDTO, orderDateTime);
        return ResponseEntity.ok(kakaoOrderResponse);
    }
}
