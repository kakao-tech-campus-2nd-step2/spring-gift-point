package gift.controller.product;

import gift.application.product.dto.OrdersModel;
import gift.application.product.service.OrdersService;
import gift.controller.product.dto.OrdersResponse;
import gift.global.auth.Authenticate;
import gift.global.auth.Authorization;
import gift.global.auth.LoginInfo;
import gift.global.dto.PageResponse;
import gift.model.member.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrdersController {

    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @Authorization(role = Role.USER)
    @GetMapping("/api/orders")
    public ResponseEntity<PageResponse<OrdersResponse.Info>> getOrders(
        @Authenticate LoginInfo loginInfo,
        @PageableDefault(page = 0, size = 5, sort = "createdAt", direction = Direction.DESC) Pageable pageable
    ) {
        Page<OrdersModel.Info> page = ordersService.getOrders(loginInfo.memberId(), pageable);
        return ResponseEntity.ok(PageResponse.from(page, OrdersResponse.Info::from));
    }
}
