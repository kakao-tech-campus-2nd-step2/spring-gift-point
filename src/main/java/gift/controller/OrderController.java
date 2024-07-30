package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.annotation.LoginMember;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.service.KakaoService;
import gift.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/orders")
@RestController
public class OrderController {

    private final OrderService orderService;
    private final KakaoService kakaoService;

    @Autowired
    public OrderController(OrderService orderService, KakaoService kakaoService) {
        this.orderService = orderService;
        this.kakaoService = kakaoService;
    }
//
//    @GetMapping
//    public ResponseEntity

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@LoginMember Long memberId,
        @Valid @RequestBody OrderRequestDto request) throws JsonProcessingException {
        OrderResponseDto response = orderService.createOrder(memberId, request);
        kakaoService.sendKakaoMessage(memberId, response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}