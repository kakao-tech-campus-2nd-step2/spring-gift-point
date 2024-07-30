package gift.controller;


import gift.dto.request.LoginMemberDTO;
import gift.dto.request.OrderRequestDTO;
import gift.service.LoginMember;
import gift.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api/orders")
@Controller
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("")
    public ResponseEntity<String> order(@LoginMember LoginMemberDTO loginMemberDTO ,
                                        @RequestBody OrderRequestDTO orderRequestDTO) {
        orderService.addOrder(loginMemberDTO, orderRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Order created");
    }


    /*@GetMapping("")
    public ResponseEntity<Map<String, Object>> getOrders(
            @LoginMember LoginMemberDTO loginMemberDTO,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderDateTime,desc") String sort
    ) {




    }*/
}
