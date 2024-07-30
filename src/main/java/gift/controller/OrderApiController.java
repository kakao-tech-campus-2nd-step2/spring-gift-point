package gift.controller;

import gift.auth.CheckRole;
import gift.auth.JwtService;
import gift.auth.LoginMember;
import gift.auth.XOAuthToken;
import gift.exception.InputException;
import gift.request.LoginMemberDto;
import gift.request.OrderRequest;
import gift.response.OrderResponse;
import gift.service.KakaoMessageService;
import gift.service.OptionsService;
import gift.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @CheckRole("ROLE_USER")
    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> makeOrder(HttpServletRequest request,
        @RequestBody @Valid OrderRequest orderRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputException(bindingResult.getAllErrors());
        }

        Long memberId = Long.valueOf(request.getAttribute("member_id").toString());
        String xOAuthToken = request.getAttribute("X-OAuth-Token").toString();
        OrderResponse dto = orderService.makeOrder(memberId, xOAuthToken, orderRequest.productId(),
            orderRequest.optionId(), orderRequest.quantity(), orderRequest.message());

        return new ResponseEntity<>(dto, HttpStatus.OK);

    }

    @CheckRole("ROLE_USER")
    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> getOrder(@LoginMember LoginMemberDto dto) {
        return new ResponseEntity<>(orderService.getOrder(dto.id()), HttpStatus.OK);
    }

}
