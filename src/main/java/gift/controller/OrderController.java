package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.betweenClient.ResponseDTO;
import gift.dto.betweenClient.member.MemberDTO;
import gift.dto.betweenClient.order.OrderRequestDTO;
import gift.dto.betweenClient.order.OrderResponseDTO;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @Hidden
    public ResponseEntity<Page<OrderResponseDTO>> getOrders(Pageable pageable){
        return new ResponseEntity<>(orderService.getOrders(pageable), HttpStatus.OK);
    }


    @PostMapping
    @Operation(description = "클라이언트가 제출한 주문을 처리하고, 카카오톡으로 주문 메세지를 보냅니다. " +
            " 클라이언트는 'Authorization' 헤더에 'Bearer {토큰}' 형식으로 토큰을 제출해야 합니다.", tags = "Order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "주문을 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 보통 주문에 옵션 정보가 잘못되었거나, 재고가 소진된 경우입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "유효하지 않는 토큰입니다.",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버에 의한 오류입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))})
    public ResponseEntity<?> order(@Parameter(hidden = true) @LoginMember MemberDTO memberDTO, @RequestBody OrderRequestDTO orderRequestDTO){
        return new ResponseEntity<>(orderService.order(memberDTO, orderRequestDTO), HttpStatus.CREATED);
    }
}