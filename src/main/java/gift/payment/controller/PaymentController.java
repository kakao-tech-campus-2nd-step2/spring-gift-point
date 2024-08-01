package gift.payment.controller;

import gift.payment.application.PaymentService;
import gift.payment.domain.CalcPaymentRequest;
import gift.payment.domain.CalcPaymentResponse;
import gift.util.CommonResponse;
import gift.util.annotation.JwtAuthenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(summary = "결제 처리", description = "사용자의 위시리스트 아이템을 결제 처리합니다.")
    @PostMapping("/orders")
    @JwtAuthenticated
    public ResponseEntity<?> processPayment(
            @Parameter(description = "위시리스트 ID") @PathVariable Long wishListId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());

        paymentService.processPayment(userId, wishListId);
        return ResponseEntity.ok(new CommonResponse<>(
                null, "결제 완료", true
        ));
    }


    @GetMapping("/orders/price")
    public ResponseEntity<?> calcPayment(@RequestBody CalcPaymentRequest request) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Long userId = Long.valueOf(authentication.getName());

        Long price = paymentService.calcPayment(request.getOptionId(), request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(new CalcPaymentResponse(price));
    }

}
