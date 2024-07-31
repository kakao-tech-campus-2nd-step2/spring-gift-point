package gift.controller;

import gift.controller.api.GiftOrderApi;
import gift.dto.giftorder.GiftOrderRequest;
import gift.dto.giftorder.GiftOrderResponse;
import gift.service.GiftOrderService;
import gift.service.KakaoService;
import gift.service.OptionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class GiftOrderController implements GiftOrderApi {

    private final GiftOrderService giftOrderService;
    private final OptionService optionService;
    private final KakaoService kakaoService;

    public GiftOrderController(GiftOrderService giftOrderService, OptionService optionService, KakaoService kakaoService) {
        this.giftOrderService = giftOrderService;
        this.optionService = optionService;
        this.kakaoService = kakaoService;
    }

    @PostMapping
    public ResponseEntity<GiftOrderResponse> orderOption(@RequestAttribute("memberId") Long memberId, @Valid @RequestBody GiftOrderRequest giftOrderRequest) {
        var order = optionService.orderOption(memberId, giftOrderRequest);
        kakaoService.sendOrderResponseWithKakaoMessage(memberId, order);
        return ResponseEntity.created(URI.create("/api/orders/" + order.id())).body(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftOrderResponse> getOrder(@PathVariable Long id) {
        var order = giftOrderService.getGiftOrder(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<GiftOrderResponse>> getOrders(@RequestAttribute("memberId") Long memberId, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        var orders = giftOrderService.getGiftOrders(memberId, pageable);
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        giftOrderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
