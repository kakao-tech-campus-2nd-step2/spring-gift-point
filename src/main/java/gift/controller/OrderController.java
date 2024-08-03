package gift.controller;

import gift.DTO.MemberDTO;
import gift.DTO.OrderDTO;
import gift.auth.LoginMember;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@Tag(name = "주문 API", description = "주문을 생성합니다.")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 새로운 주문을 생성
     *
     * @param memberDTO 로그인된 회원 정보
     * @param orderDTO  주문 정보
     * @return 생성된 주문 정보를 담은 OrderDTO 객체
     */
    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청으로 인한 주문 생성 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류로 인한 주문 생성 실패")
    })
    @PostMapping
    public OrderDTO createOrder(@LoginMember MemberDTO memberDTO, @RequestBody OrderDTO orderDTO) {
        return orderService.createOrder(orderDTO, memberDTO);
    }
}
