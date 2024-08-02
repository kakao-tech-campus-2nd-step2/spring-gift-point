package gift.controller;

import gift.domain.ProductOrder;
import gift.domain.ProductOrder.OrderDetail;
import gift.domain.ProductOrder.OrderSimple;
import gift.domain.ProductOrder.decreaseProductOption;
import gift.service.ProductOrderService;
import gift.util.page.PageMapper;
import gift.util.page.PageResult;
import gift.util.page.SingleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "상품 주문 관련 서비스")
@RestController
@RequestMapping("/api/product/order")
public class ProductOrderController {

    private final ProductOrderService productOrderService;

    @Autowired
    public ProductOrderController(ProductOrderService productOrderService) {
        this.productOrderService = productOrderService;
    }

    @Operation(summary = "상품 주문", description = "어느 유저가 요구만큼 주문, 주문 성공시 카카오톡 메세지 송신")
    @PostMapping("/{optionId}")
    public SingleResult<OrderDetail> decreaseProductOption(
        HttpServletRequest req,
        @PathVariable Long optionId, @Valid @RequestBody decreaseProductOption decrease) {
        return new SingleResult<>(
            productOrderService.decreaseProductOption(req, optionId, decrease));
    }

    @Operation(summary = "상품 주문 리스트 조회", description = "본인 상품 주문 내역 리스트 반환")
    @GetMapping()
    public PageResult<OrderSimple> ProductOrderList(
        HttpServletRequest req, @Valid @RequestBody ProductOrder.getList param) {
        return PageMapper.toPageResult(productOrderService.getOrderList(req,param));
    }
}
