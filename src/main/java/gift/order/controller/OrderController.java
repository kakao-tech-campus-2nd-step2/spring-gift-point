package gift.order.controller;

import gift.auth.argumentResolver.KakaoMember;
import gift.auth.service.KakaoMsgService;
import gift.order.dto.OrderRequest;
import gift.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/member/{memberId}/order")
@RestController
public class OrderController {

    private final OrderService orderService;

    private final KakaoMsgService kakaoMsgService;

    public OrderController(OrderService orderService, KakaoMsgService kakaoMsgService) {
        this.orderService = orderService;
        this.kakaoMsgService = kakaoMsgService;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createOrder(@KakaoMember String token, @RequestBody OrderRequest orderRequest) {
        orderService.create(orderRequest);
        kakaoMsgService.sendMsg(token,orderRequest);

        return ResponseEntity.ok().build();
    }
}
