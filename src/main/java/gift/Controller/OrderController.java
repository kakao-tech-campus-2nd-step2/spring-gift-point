package gift.Controller;

import gift.Annotation.LoginMemberResolver;
import gift.Model.MemberDto;
import gift.Model.OrderRequestDto;
import gift.Service.KakaoTalkService;
import gift.Service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/orders")
@Tag(name = "Order", description = "주문 관련 api")
public class OrderController {

    private final OrderService orderService;
    private final KakaoTalkService kakaoTalkService;

    @Autowired
    public OrderController(OrderService orderService, KakaoTalkService kakaoTalkService) {
        this.orderService = orderService;
        this.kakaoTalkService = kakaoTalkService;
    }

    @PostMapping("/")
    @Operation(summary = "주문 생성", description = "주문을 생성합니다.")
    public ResponseEntity<String> purchaseWishlist(@LoginMemberResolver MemberDto memberDto, @RequestBody List<OrderRequestDto> orderRequestDtoList, HttpServletRequest request) {
        for (OrderRequestDto orderRequestDto : orderRequestDtoList) {
            orderRequestDto.setMemberId(memberDto.getId());
        }

        //카카오 토큰 가져오기
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        //토큰으로 메시지 보내기
        if (token != null) { //
            try {
                int totalPrice = orderService.calculateTotalPrice(orderRequestDtoList);
                kakaoTalkService.sendMessageToMe(token, orderRequestDtoList, totalPrice);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Error sending message");
            }
        }

        return ResponseEntity.ok("Purchase successful");
    }
}
