package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.dto.OrderPageResponseDto;
import gift.entity.User;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "주문 관련 API")
public class OrderController {

    private final OrderService orderService;
    private static final int DEFAULT_SIZE = 20;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "주문이 성공적으로 생성되었습니다.",
                    content = @Content(
                            schema = @Schema(implementation = OrderResponseDto.class)
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청. 예: 수량이 부족하거나 유효하지 않은 요청 데이터가 포함된 경우.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "상품 옵션을 찾을 수 없음.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<OrderResponseDto> createOrder(
            @LoginMember User loginUser,
            @Parameter(description = "Kakao 인증 토큰", required = true) @RequestHeader("Kakao-Authorization") String kakaoAccessToken,
            @Parameter(description = "생성할 주문의 정보", required = true) @RequestBody OrderRequestDto requestDto) {
        OrderResponseDto responseDto = orderService.createOrder(loginUser, kakaoAccessToken, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "주문 목록 조회", description = "사용자의 주문 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 목록이 성공적으로 반환되었습니다.",
                    content = @Content(
                            schema = @Schema(implementation = OrderPageResponseDto.class)
                    )),
            @ApiResponse(responseCode = "500", description = "서버 오류.",
                    content = @Content)
    })
    public ResponseEntity<OrderPageResponseDto> getUserOrders(
            @LoginMember User loginUser,
            @Parameter(description = "페이지 번호 (0부터 시작)", required = false, schema = @Schema(type = "integer", defaultValue = "0")) @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, DEFAULT_SIZE);
        OrderPageResponseDto orderPage = orderService.getUserOrders(loginUser.getId(), pageable);
        return new ResponseEntity<>(orderPage, HttpStatus.OK);
    }
}
