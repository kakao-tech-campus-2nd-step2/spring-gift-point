package gift.controller;

import gift.annotation.LoginUser;
import gift.dto.OrderRequestDTO;
import gift.entity.Member;
import gift.entity.Option;
import gift.entity.Orders;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @Operation(summary = "주문 목록 조회", description = "로그인한 사용자의 주문 목록을 페이지별로 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<Orders>> getOrderList(
        @LoginUser String email,
        @Parameter(name = "page", description = "페이지 번호", example = "1")
        @RequestParam int page,
        @Parameter(name = "size", description = "페이지 크기", example = "20")
        @RequestParam int size,
        @Parameter(name = "sort", description = "정렬 기준", example = "id,desc")
        @RequestParam String[] sort){
        Page<Orders> order = orderService.getProductPage(email,page,size,sort);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @Operation(summary = "상품 주문", description = "사용자가 상품을 주문합니다.")
    @PostMapping
    public ResponseEntity<String> addOrder(@RequestBody OrderRequestDTO orderRequest, @LoginUser String email){
        Option option = orderService.findOptionById(orderRequest.getOptionId());
        Member member = orderService.findMemberByEmail(email);
        orderService.orderOption(orderRequest,email);
        return new ResponseEntity<>("상품 추가 완료",HttpStatus.OK);
    }



}
