package gift.controller;

import gift.domain.AuthToken;
import gift.dto.request.OrderRequestDto;
import gift.dto.response.OrderResponseDto;
import gift.service.OrderService;
import gift.service.TokenService;
import gift.utils.PageableUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<Page<OrderResponseDto>> getOrders(HttpServletRequest request,
                                                            @RequestParam(name = "page", defaultValue = "0") int page,
                                                            @RequestParam(name = "size", defaultValue = "10") int size,
                                                            @RequestParam(name = "sort", defaultValue = "createdDate,desc") String sortBy
    ){
        AuthToken token = getAuthVO(request);
        Pageable pageable = PageableUtils.createPageable(page, size, sortBy);
        Page<OrderResponseDto> orderResponseDtos = orderService.findOrdersUsingPaging(pageable, token.getEmail());
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(orderResponseDtos);
    }

    public AuthToken getAuthVO(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return tokenService.findToken(token);
    }
}