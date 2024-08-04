package gift.orderOption.controller;

import gift.global.argumentresolver.LoginMember;
import gift.member.dto.MemberDto;
import gift.orderOption.dto.OrderOptionRequest;
import gift.orderOption.dto.OrderOptionResponse;
import gift.kakao.service.KakaoTalkService;
import gift.orderOption.service.OrderOptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order", description = "주문 관련 API")
@RestController
@RequestMapping("/api/orders")
public class OrderOptionController {

    private final OrderOptionService orderOptionService;
    private final KakaoTalkService kakaoTalkService;

    @Autowired
    public OrderOptionController(OrderOptionService orderOptionService, KakaoTalkService kakaoTalkService) {
        this.orderOptionService = orderOptionService;
        this.kakaoTalkService = kakaoTalkService;
    }

    @Operation(summary = "주문", description = "해당 옵션을 주문합니다.")
    @PostMapping
    public ResponseEntity<OrderOptionResponse> order(
        @LoginMember MemberDto memberDto,
        @Valid @RequestBody OrderOptionRequest orderOptionRequest
    ) {
        return ResponseEntity.ok().body(orderOptionService.order(memberDto, orderOptionRequest));
    }
}
