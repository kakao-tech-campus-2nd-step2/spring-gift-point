package gift.controller;

import gift.argumentresolver.LoginMember;
import gift.dto.KakaoTalkResponse;
import gift.dto.MemberDTO;
import gift.dto.OrderOptionDTO;
import gift.service.KakaoTalkService;
import gift.service.OrderOptionService;
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
    public ResponseEntity<KakaoTalkResponse> order(@LoginMember MemberDTO memberDTO, @Valid @RequestBody OrderOptionDTO orderOptionDTO) {
        return ResponseEntity.ok().body(orderOptionService.order(memberDTO, orderOptionDTO));
    }
}
