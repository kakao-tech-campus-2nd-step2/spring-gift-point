package gift.controller;

import gift.domain.ProductOption.optionDetail;
import gift.domain.ProductOrder.decreaseProductOption;
import gift.service.ProductOrderService;
import gift.util.page.SingleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "상품 주문 관련 서비스")
@RestController
@RequestMapping("/api/product/{productId}/order")
public class ProductOrderController {

    private final ProductOrderService productOrderService;

    @Autowired
    public ProductOrderController(ProductOrderService productOrderService) {
        this.productOrderService = productOrderService;
    }

    @Operation(summary = "상품 주문", description = "어느 유저가 요구만큼 주문, 주문 성공시 카카오톡 메세지 송신")
    @PostMapping("/{optionId}")
    public SingleResult<optionDetail> decreaseProductOption(
        HttpServletRequest req,
        @PathVariable Long productId,
        @PathVariable Long optionId, @Valid @RequestBody decreaseProductOption decrease) {
        return new SingleResult<>(
            productOrderService.decreaseProductOption(req, productId, optionId, decrease));
    }
}
