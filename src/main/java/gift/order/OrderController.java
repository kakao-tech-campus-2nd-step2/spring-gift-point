package gift.order;

import gift.jwt.Login;
import gift.jwt.OAuth;
import gift.member.UserDTO;
import gift.wishes.WishRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "order", description = "order API")
@Controller
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "주문하기", description = "주문한다",
        parameters = {
            @Parameter(
                in = ParameterIn.HEADER,
                name = "X-GATEWAY-TOKEN",
                required = true,
                schema = @Schema(type = "string"),
                description = "카카오톡 메시지를 보내기 위한 액세스 토큰입니다."
            )
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = WishRequest.class))
        )
    )
    @PostMapping()
    public ResponseEntity<OrderInfo> order(@RequestBody OrderRequest orderRequest,
        @OAuth String accessToken, @Login
    UserDTO userDTO) {
        OrderInfo order = orderService.saveOrder(orderRequest, userDTO.getUserId());
        orderService.sendMessage(orderRequest, accessToken);
        orderService.deleteOrderedProduct(orderRequest, userDTO.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @Operation(summary = "주문 목록 조회", description = "주문 목록을 조회한다.")
    @GetMapping()
    public ResponseEntity<OrderContents> getOrderList(@Login UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(OrderContents.from(orderService.getOrderList(userDTO.getUserId())));
    }


}
