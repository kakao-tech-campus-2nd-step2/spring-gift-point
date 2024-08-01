package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.annotation.LoginMember;
import gift.dto.ErrorResponseDto;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.service.KakaoService;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/orders")
@RestController
@Tag(name = "Order", description = "주문 관리 API")
public class OrderController {

    private final OrderService orderService;
    private final KakaoService kakaoService;

    @Autowired
    public OrderController(OrderService orderService, KakaoService kakaoService) {
        this.orderService = orderService;
        this.kakaoService = kakaoService;
    }

    @Operation(summary = "주문 목록 조회", description = "로그인한 사용자의 모든 주문 목록을 페이지별로 조회한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "주문 목록 조회 성공", content = @Content(schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping
    public ResponseEntity<Page<OrderResponseDto>> getOrders(
        @LoginMember Long memberId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "id,desc") String[] sort) {
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        Page<OrderResponseDto> orderList = orderService.findAll(memberId, pageable);

        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }

    @Operation(summary = "상품 주문", description = "로그인한 사용자가 상품을 주문한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "주문 생성 성공", content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@LoginMember Long memberId,
        @RequestBody @Valid OrderRequestDto request) throws JsonProcessingException {
        OrderResponseDto response = orderService.createOrder(memberId, request);
        kakaoService.sendKakaoMessage(memberId, response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}