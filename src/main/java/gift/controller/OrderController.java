package gift.controller;

import gift.domain.AuthToken;
import gift.dto.request.OrderRequestDto;
import gift.dto.response.OrderResponseDto;
import gift.service.OrderService;
import gift.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/orders")
public class OrderController {

    private final TokenService tokenService;
    private final OrderService orderService;

    public OrderController(TokenService tokenService, OrderService orderService) {
        this.tokenService = tokenService;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> addOrder(HttpServletRequest request,
                                                     @RequestBody @Valid OrderRequestDto orderRequestDto) {
        AuthToken token = getAuthVO(request);
        OrderResponseDto orderResponseDto = orderService.addOrder(orderRequestDto, token);
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(orderResponseDto);
    }

    public AuthToken getAuthVO(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return tokenService.findToken(token);
    }
}