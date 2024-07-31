package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.annotation.LoginMember;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.service.KakaoService;
import gift.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping
    public ResponseEntity<Page<OrderResponseDto>> getOrders(
        @LoginMember Long memberId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "id,desc") String[] sort) {
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        Page<OrderResponseDto> orderList = orderService.findAll(memberId, pageable);

        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@LoginMember Long memberId,
        @RequestBody @Valid OrderRequestDto request) throws JsonProcessingException {
        OrderResponseDto response = orderService.createOrder(memberId, request);
        kakaoService.sendKakaoMessage(memberId, response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}