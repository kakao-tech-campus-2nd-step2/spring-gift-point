package gift.order.controller;

import gift.auth.domain.AuthInfo;
import gift.global.exception.DomainValidationException;
import gift.global.response.ErrorResponseDto;
import gift.global.response.ResultCode;
import gift.global.response.ResultResponseDto;
import gift.global.response.SimpleResultResponseDto;
import gift.global.security.Login;
import gift.global.utils.ResponseHelper;
import gift.order.dto.OrderListResponseDto;
import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("")
    public ResponseEntity<ResultResponseDto<OrderListResponseDto>> getAllOrders() {
        OrderListResponseDto orderListResponseDto = orderService.getAllOrders();
        return ResponseHelper.createResponse(ResultCode.GET_ALL_OPTIONS_SUCCESS, orderListResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponseDto<OrderResponseDto>> getOrderById(@PathVariable(name = "id") Long id) {
        OrderResponseDto orderResponseDto = orderService.getOrderById(id);
        return ResponseHelper.createResponse(ResultCode.GET_OPTION_BY_ID_SUCCESS, orderResponseDto);
    }

    @PostMapping("")
    public ResponseEntity<SimpleResultResponseDto> createOrder(@Login AuthInfo authInfo, @RequestBody OrderRequestDto orderRequestDto) {
        orderService.createOrder(orderRequestDto.toOrderServiceDto(authInfo.memberId()));
        return ResponseHelper.createSimpleResponse(ResultCode.CREATE_OPTION_SUCCESS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SimpleResultResponseDto> updateOrder(@PathVariable(name = "id") Long id, @RequestBody OrderRequestDto orderRequestDto) {
        orderService.updateOrder(orderRequestDto.toOrderServiceDto(id));
        return ResponseHelper.createSimpleResponse(ResultCode.UPDATE_OPTION_SUCCESS);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SimpleResultResponseDto> deleteOrder(@PathVariable(name = "id") Long id) {
        orderService.deleteOrder(id);
        return ResponseHelper.createSimpleResponse(ResultCode.DELETE_OPTION_SUCCESS);
    }

    // GlobalException Handler 에서 처리할 경우,
    // RequestBody에서 발생한 에러가 HttpMessageNotReadableException 로 Wrapping 이 되는 문제가 발생한다
    // 때문에, 해당 에러로 Wrapping 되기 전 Controller 에서 Domain Error 를 처리해주었다
    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleOptionValidException(DomainValidationException e) {
        System.out.println(e);
        return ResponseHelper.createErrorResponse(e.getErrorCode());
    }
}
