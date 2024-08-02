package gift.payment.controller;

import gift.payment.application.PaymentService;
import gift.payment.domain.CalcPaymentRequest;
import gift.payment.domain.CalcPaymentResponse;
import gift.payment.domain.PaymentRequest;
import gift.payment.domain.PaymentResponse;
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

    @PostMapping("/orders")
    @JwtAuthenticated
    public ResponseEntity<?> processPayment(
            @RequestBody PaymentRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());

        PaymentResponse paymentResponse = paymentService.processPayment(userId, request);
        return ResponseEntity.ok(paymentResponse);
    }


    @GetMapping("/orders/price")
    public ResponseEntity<?> calcPayment(@RequestBody CalcPaymentRequest request) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Long userId = Long.valueOf(authentication.getName());

        Long price = paymentService.calcPayment(request.getOptionId(), request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(new CalcPaymentResponse(price));
    }

}
