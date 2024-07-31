package gift.product.application;

import gift.auth.interceptor.Authorized;
import gift.auth.resolver.LoginMember;
import gift.product.application.dto.request.ProductPurchaseRequest;
import gift.product.service.facade.ProductFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ProductPurchase", description = "상품 구매 관련 API")
@RestController
@RequestMapping("/api/products/{productId}/options/purchase")
public class PurchaseController {
    private final ProductFacade productFacade;

    public PurchaseController(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    @Operation(summary = "상품 구매", description = "상품을 구매합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 구매 성공"),
            @ApiResponse(responseCode = "207", description = "상품 구매 성공, 카카오톡 알림 전송 실패")
    })
    @PostMapping()
    @Authorized()
    @ResponseStatus(HttpStatus.OK)
    public void purchaseProduct(@PathVariable("productId") Long productId,
                                @LoginMember Long loginMember,
                                @RequestBody ProductPurchaseRequest productPurchaseRequest
    ) {
        var command = productPurchaseRequest.toCommand(productId, loginMember);

        productFacade.purchaseProduct(command);
    }

}
