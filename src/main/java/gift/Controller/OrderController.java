package gift.Controller;

import gift.DTO.OrderDTO;
import gift.DTO.OrderDTOTest;
import gift.DTO.OrderRequestDTO;
import gift.DTO.OrderResponseDTO;
import gift.Entity.OrderEntity;
import gift.Service.OrderService;
import gift.util.CustomPageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product/order")
@Tag(name = "상품 주문 관련 서비스", description = " ")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "상품 주문", description = "어느 유저가 요구만큼 주문, 주문 성공시 카카오톡 메세지 송신")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created order"),
            @ApiResponse(responseCode = "400", description = "Invalid order request data")
    })
    @PostMapping("/{optionId}")
    public CustomPageResponse<OrderDTO> placeOrder(@PathVariable Long optionId, @RequestBody OrderDTO orderDTO) {
        OrderEntity order = orderService.placeOrder(optionId, orderDTO.getQuantity());

        OrderDTO resultData = new OrderDTO(
                order.getProduct().getId(),
                order.getProduct().getName(),
                order.getProduct().getPrice(),
                order.getOption().getId(),
                order.getQuantity(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getOption().getName()
        );

        List<OrderDTO> resultDataList = Arrays.asList(resultData);

        return new CustomPageResponse<>(
                resultDataList,
                0,
                1,
                false,
                1L
        );
    }

    @Operation(summary = "상품 주문 목록 조회", description = " ")
    @GetMapping
    public ResponseEntity<CustomPageResponse<OrderDTO>> getOrders(
            @RequestParam(defaultValue = "3") int page,
            @RequestParam(defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDTO> orderPage = orderService.getOrders(pageable);

        CustomPageResponse<OrderDTO> response = new CustomPageResponse<>(
                orderPage.getContent(),
                page,
                orderPage.getTotalPages(),
                orderPage.hasNext(),
                orderPage.getTotalElements()
        );

        return ResponseEntity.ok(response);
    }




//    @PostMapping
//    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
//        OrderResponseDTO orderResponseDTO = orderService.createOrder(orderRequestDTO);
//        return ResponseEntity.ok(orderResponseDTO);
//    }
}
