package gift.main.controller;

import gift.main.annotation.SessionUser;
import gift.main.dto.OrderRequest;
import gift.main.dto.OrderResponse;
import gift.main.dto.UserVo;
import gift.main.service.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final KakaoService kakaoService;
    private final OptionService optionService;
    private final ApiTokenService apiTokenService;
    private final WishProductService wishProductService;

    public OrderController(OrderService orderService,
                           KakaoService kakaoService,
                           OptionService optionService,
                           ApiTokenService apiTokenService,
                           WishProductService wishProductService) {
        this.orderService = orderService;
        this.kakaoService = kakaoService;
        this.optionService = optionService;
        this.apiTokenService = apiTokenService;
        this.wishProductService = wishProductService;
    }

    /**
     * 새롭게 주문하기
     * 1. 주문 가능한 상태인지 알아보고 해당 옵션 수량 삭제하기
     * 2. 주문 엔티티 추가하기
     * 3. 위시리스트에서 삭제하기
     * 4. 주문 메시지 보내기 (보내기 전에 토큰 갱신하기)
     */
    @PostMapping()
    public ResponseEntity<?> orderProduct(@Valid @RequestBody OrderRequest orderRequest, @SessionUser UserVo sessionUserVo) {
        //1. 주문 가능한 상태인지 알아보고 해당 옵션 수량 삭제하기
        optionService.removeOptionQuantityFromOrder(orderRequest.optionId(), orderRequest.quantity()); //이 부분을 바꿀까 생각 중

        //2. 주문 엔티티 추가하기
        OrderResponse orderResponse = orderService.orderProduct(orderRequest, sessionUserVo); //상품을 주문하고, 해당 상품의 옵션을 제거해야한다.

        //3. 위시리스트에서 삭제하기
        wishProductService.deleteWishProductsFromOrders(orderResponse);

        //4. 주문 메시지 보내기 (보내기 전에 토큰 갱신하기)
//        apiTokenService.renewToken(sessionUserVo);
//        kakaoService.sendOrderMessage(orderResponse, sessionUserVo);


        return ResponseEntity.ok(orderResponse);
    }

}
