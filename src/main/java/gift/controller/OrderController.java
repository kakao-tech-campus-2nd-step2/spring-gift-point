package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.dto.OrderDTO;
import gift.model.valueObject.BearerToken;
import gift.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(KakaoController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public void orderProduct(HttpServletRequest request, @RequestBody OrderDTO orderDTO) throws JsonProcessingException {
        BearerToken token = (BearerToken) request.getAttribute("bearerToken");
        orderService.orderProduct(token.getToken(), orderDTO);
    }
}
