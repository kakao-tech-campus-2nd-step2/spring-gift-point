package gift.controller;

import gift.dto.request.OrderRequest;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "[협업] ORDER API", description = "[협업] 주문 컨트롤러")
public class OrderNewController {

    private OptionService optionService;

    @Autowired
    public OrderNewController(OptionService optionService) {
        this.optionService = optionService;
    }


    @PostMapping
    @Operation(summary = "상품 주문", description = "상품을 주문")
    public ResponseEntity<Void> orderItem(@RequestBody OrderRequest request,
                                          @RequestAttribute("userId") Long userId) {
        Long optionId = request.getOptionId();
        int quantity = request.getQuantity();
        optionService.subtractOptionQuantity(optionId, quantity);
        return ResponseEntity.ok().build();
    }
}
