package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.constants.SuccessMessage;
import gift.dto.OrderFindAllResponse;
import gift.dto.OrderRequest;
import gift.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/list")
    public PagedModel<OrderFindAllResponse> findAllOrders(
        @PageableDefault(size = 5) Pageable pageable) {
        return new PagedModel<>(orderService.findAllOrders(pageable));
    }

    @PostMapping
    public ResponseEntity<String> order(@RequestBody OrderRequest orderRequest,
        HttpServletRequest httpServletRequest) throws JsonProcessingException {

        String email = (String) httpServletRequest.getAttribute("email");
        orderService.order(orderRequest, email);

        return ResponseEntity.ok(SuccessMessage.KAKAO_SEND_MESSAGE_SUCCESS_MSG);
    }
}