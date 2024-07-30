package gift.controller;

import gift.auth.LoginUser;
import gift.domain.Option;
import gift.domain.User;
import gift.dto.common.apiResponse.ApiResponseBody.SuccessBody;
import gift.dto.common.apiResponse.ApiResponseGenerator;
import gift.dto.requestdto.OrderRequestDTO;
import gift.dto.responsedto.OrderResponseDTO;
import gift.service.OptionService;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Parameter;


@RestController
@RequestMapping("/api/orders")
@Tag(name = "주문 api", description = "주문 api입니다")
public class OrderController {
    private final OptionService optionService;
    private final OrderService orderService;

    public OrderController(OptionService optionService, OrderService orderService) {
        this.optionService = optionService;
        this.orderService = orderService;
    }

    @PostMapping()
    @Operation(summary = "주문 api", description = "주문 api입니다")
    @ApiResponse(responseCode = "201", description = "주문 성공")
    public ResponseEntity<SuccessBody<OrderResponseDTO>> createOrder(
        @LoginUser User user,
        @Valid @RequestBody OrderRequestDTO orderRequestDTO
    ) {
        Option option = optionService.getOption(orderRequestDTO.optionId());
        OrderResponseDTO orderResponseDTO = orderService.order(orderRequestDTO, user, option);
        return ApiResponseGenerator.success(HttpStatus.CREATED, "주문이 생성되었습니다.", orderResponseDTO);
    }

}
