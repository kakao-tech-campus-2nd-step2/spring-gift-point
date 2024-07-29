package gift.controller;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.model.LoginMember;
import gift.model.Member;
import gift.service.OrderProcessingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/orders")
@RestController
public class OrderController {
    private final OrderProcessingService orderProcessingService;

    public OrderController(OrderProcessingService orderProcessingService) {
        this.orderProcessingService = orderProcessingService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> make(@RequestBody @Valid OrderRequest request, @LoginMember Member member) {
        OrderResponse response = orderProcessingService.orderProcess(request, member);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
