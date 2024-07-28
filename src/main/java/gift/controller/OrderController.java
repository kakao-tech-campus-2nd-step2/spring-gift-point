package gift.controller;

import gift.annotation.LoginMember;
import gift.domain.Member;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.service.OrderService;
import gift.service.ProductService;
import gift.service.WishService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final ProductService productService;
    private final WishService wishService;
    private final OrderService orderService;

    public OrderController(ProductService productService, WishService wishService,
        OrderService orderService) {
        this.productService = productService;
        this.wishService = wishService;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto orderRequestDto, @LoginMember Member member) {
        productService.decreaseOptionQuantity(orderRequestDto.productId(), orderRequestDto.optionId(),orderRequestDto.quantity());
        wishService.deleteProductFromWishList(member.getId(), orderRequestDto.productId());
        OrderResponseDto orderResponseDto = orderService.createOrder(orderRequestDto);

        orderService.sendOrderMessage(orderRequestDto, member);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDto);
    }
}
