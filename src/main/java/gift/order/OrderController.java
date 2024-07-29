package gift.order;

import gift.annotation.LoginUser;
import gift.option.OptionResponse;
import gift.option.OptionService;
import gift.user.IntegratedUser;
import gift.user.KakaoUser;
import gift.wishList.WishList;
import gift.wishList.WishListService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

        OptionResponse optionResponse = optionService.subtractOptionQuantity(request.getOptionId(), request.getQuantity());
        Optional<WishList> wishList = wishListService.findByKakaoUserAndOptionID(request.getOptionId(), (KakaoUser) kakaoUser);

        wishList.ifPresent(list -> wishListService.deleteByID(list.getId(), kakaoUser));

        return orderService.sendMessage((KakaoUser) kakaoUser, request, optionResponse);

    }

}
