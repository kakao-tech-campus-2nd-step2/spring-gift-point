package gift.order;

import gift.annotation.LoginUser;
import gift.option.OptionResponse;
import gift.option.OptionService;
import gift.product.ProductResponse;
import gift.user.IntegratedUser;
import gift.user.KakaoUser;
import gift.wishList.WishList;
import gift.wishList.WishListResponse;
import gift.wishList.WishListService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class OrderController {
    private final OrderService orderService;
    private final WishListService wishListService;
    private final OptionService optionService;

    public OrderController(OrderService orderService, WishListService wishListService, OptionService optionService) {
        this.orderService = orderService;
        this.wishListService = wishListService;
        this.optionService = optionService;

    }

    @PostMapping("/api/orders")
    public OrderResponse OrderProduct(@LoginUser IntegratedUser kakaoUser, @RequestBody OrderRequest request){
        KakaoUser user = (KakaoUser) kakaoUser;
        Long point = user.getPoint();
        Long usePoint = request.getPoint();
        if(point < usePoint) throw new IllegalArgumentException("사용하려는 포인트가 현재 보유한 포인트를 초과합니다.");
        Long sum = request.getQuantity() * optionService.getPrice(request.getOptionId());
        if(sum < usePoint) throw new IllegalArgumentException("사용하려는 포인트가 상품 가격을 초과합니다.");

        OptionResponse optionResponse = optionService.subtractOptionQuantity(request.getOptionId(), request.getQuantity());
        Optional<List<WishList>> wishList = wishListService.findByKakaoUserAndOptionID(request.getOptionId(), user);

        wishList.ifPresent(list -> {
            list.forEach(wish -> wishListService.deleteByID(wish.getId(), kakaoUser));
        });

        user.usePoint(usePoint);
        sum -= usePoint;
        user.chargePoint((long) (sum * 0.05));

        return orderService.sendMessage((KakaoUser) kakaoUser, request, optionResponse);

    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> getOrderPages(@LoginUser IntegratedUser user,
                                                                @RequestParam(required = false, defaultValue = "0", value = "page") int page,
                                                                @RequestParam(required = false, defaultValue = "10", value = "size") @Min(1) @Max(20) int size,
                                                                @RequestParam(required = false, defaultValue = "id", value = "sort") List<String> sort) {

        return ResponseEntity.ok(orderService.getOrderPages(page, size, sort));

    }
}
