package gift.Controller;

import gift.DTO.RequestOrderDTO;
import gift.DTO.ResponseOrderDTO;
import gift.Model.Entity.Member;
import gift.Service.OrderService;
import gift.annotation.ValidUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Order", description = "Order API")
@RestController
@RequestMapping("api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "주문 조회", description = "주문 목록을 조회합니다")
    @ApiResponse(responseCode = "200", description = "조회 완료",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ResponseOrderDTO.class)))
    })
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 입력값을 확인해주세요")
    @ApiResponse(responseCode = "500", description = "서버 내부 에러 발생")
    @GetMapping
    public ResponseEntity<List<ResponseOrderDTO>> getOrders(@ValidUser Member member){
        List<ResponseOrderDTO> orders = orderService.getOrders(member);
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "주문 추가", description = "주문을 추가합니다")
    @ApiResponse(responseCode = "201", description = "추가 완료")
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 입력값을 확인해주세요")
    @ApiResponse(responseCode = "500", description = "서버 내부 에러 발생")
    @PostMapping
    public ResponseEntity<ResponseOrderDTO> createOrder(@ValidUser Member member, @Valid @RequestBody RequestOrderDTO requestOrderDTO){
        ResponseOrderDTO response =orderService.createOrder(member, requestOrderDTO);
        return ResponseEntity.created(URI.create("api/orders/"+response.getId())).body(response);
    }

    @Operation(summary = "주문 수정", description = "주문을 수정합니다")
    @ApiResponse(responseCode = "200", description = "수정 완료")
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 입력값을 확인해주세요")
    @ApiResponse(responseCode = "500", description = "서버 내부 에러 발생")
    @PutMapping()
    public ResponseEntity<String> editOrder(@ValidUser Member member,
                                            @RequestParam("order-id") Long orderId,
                                            @RequestParam("edit-type") String editType,
                                            @RequestParam("delta-quantity") int delataQuantity){
        orderService.editOrder(member, orderId, editType, delataQuantity);
        return ResponseEntity.ok("주문이 정상적으로 수정되었습니다");
    }

    @Operation(summary = "주문 삭제", description = "주문을 삭제합니다")
    @ApiResponse(responseCode = "200", description = "삭제 완료")
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 입력값을 확인해주세요")
    @ApiResponse(responseCode = "500", description = "서버 내부 에러 발생")
    @DeleteMapping()
    public ResponseEntity<String > deleteOrder(@ValidUser Member member, @RequestParam("order-id") Long orderId){
        orderService.deleteOrder(member, orderId);
        return ResponseEntity.ok("주문이 정상적으로 취소되었습니다");
    }
 }
