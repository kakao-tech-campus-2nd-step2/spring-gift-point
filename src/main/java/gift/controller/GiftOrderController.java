package gift.controller;

import gift.dto.giftorder.GiftOrderResponse;
import gift.service.GiftOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/giftOrders")
@Tag(name = "GIFT_ORDER")
public class GiftOrderController {

    private final GiftOrderService giftOrderService;

    public GiftOrderController(GiftOrderService giftOrderService) {
        this.giftOrderService = giftOrderService;
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
