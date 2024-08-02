package gift.controller;


import gift.dto.request.LoginMemberDTO;
import gift.dto.request.OrderRequestDTO;
import gift.dto.response.PagingOrderResponseDTO;
import gift.dto.response.PagingWishResponseDTO;
import gift.service.LoginMember;
import gift.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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


    @GetMapping("")
    public ResponseEntity<PagingOrderResponseDTO> getOrders(@LoginMember LoginMemberDTO loginMemberDTO,
                                                         @RequestParam(name = "page", defaultValue = "0") int page,
                                                         @RequestParam(name = "size" , defaultValue = "10") int size,
                                                         @RequestParam(name = "sort", defaultValue = "orderDateTime,desc") String sort) {
        String[] sortParams = sort.split(",");
        Sort.Order order = new Sort.Order(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        PagingOrderResponseDTO pagingOrderResponseDTO = orderService.getOrders(loginMemberDTO, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(pagingOrderResponseDTO);

    }
}
