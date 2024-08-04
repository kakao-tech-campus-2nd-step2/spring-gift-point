package gift.product.controller.order;

import gift.product.dto.auth.LoginMemberIdDto;
import gift.product.dto.order.OrderDto;
import gift.product.dto.order.OrderResponse;
import gift.product.exception.ExceptionResponse;
import gift.product.model.Order;
import gift.product.service.OrderService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "order", description = "주문 실행 및 주문 내역 관련 API")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final String KAKAO_SEND_MESSAGE_ME_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderResponse.class)))),
        @ApiResponse(responseCode = "401", description = "허용되지 않는 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrderAll(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "5") int size,
        @RequestParam(name = "sort", defaultValue = "orderDateTime,asc") String sortParam,
        HttpServletRequest request) {
        String[] sortParamSplited = sortParam.split(",");
        String sort = sortParamSplited[0];
        String direction = (sortParamSplited.length > 1) ? sortParamSplited[1] : "asc";

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        LoginMemberIdDto loginMemberIdDto = getLoginMember(request);

        return ResponseEntity.ok(orderService.getOrderAll(pageable, loginMemberIdDto));
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "401", description = "허용되지 않는 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable(name = "id") Long id,
        HttpServletRequest request) {
        LoginMemberIdDto loginMemberIdDto = getLoginMember(request);
        Order order = orderService.getOrder(id, loginMemberIdDto);

        return ResponseEntity.ok(order);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "주문 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "401", description = "허용되지 않는 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Order> doOrder(@RequestBody OrderDto orderDto,
        HttpServletRequest request) {
        LoginMemberIdDto loginMemberIdDto = getLoginMember(request);
        Order order = orderService.doOrder(orderDto, loginMemberIdDto, KAKAO_SEND_MESSAGE_ME_URL);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "주문 내역 삭제 성공"),
        @ApiResponse(responseCode = "401", description = "허용되지 않는 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable(name = "id") Long id,
        HttpServletRequest request) {
        LoginMemberIdDto loginMemberIdDto = getLoginMember(request);
        orderService.deleteOrder(id, loginMemberIdDto);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private LoginMemberIdDto getLoginMember(HttpServletRequest request) {
        return new LoginMemberIdDto((Long) request.getAttribute("id"));
    }
}