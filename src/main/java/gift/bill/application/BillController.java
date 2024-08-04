package gift.bill.application;

import gift.auth.resolver.LoginMember;
import gift.bill.application.dto.BillResponse;
import gift.bill.application.dto.ProductPurchaseRequest;
import gift.bill.service.BillFacadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class BillController {
    private final BillFacadeService billFacadeService;

    public BillController(BillFacadeService billFacadeService) {
        this.billFacadeService = billFacadeService;
    }

    @PostMapping
    public ResponseEntity<BillResponse> createOrder(@LoginMember Long loginMember,
                                                    @RequestBody ProductPurchaseRequest productPurchaseRequest
    ) {
        var billInfo = billFacadeService.purchaseProduct(productPurchaseRequest.toCommand(loginMember));

        return ResponseEntity.ok()
                .body(BillResponse.from(billInfo));
    }
}
