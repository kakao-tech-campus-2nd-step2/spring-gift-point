package gift.order.service;

import gift.order.model.OrderRepository;
import gift.order.model.dto.Order;
import gift.order.model.dto.OrderRequest;
import gift.order.model.dto.OrderResponse;
import gift.product.model.dto.option.Option;
import gift.product.model.dto.product.Product;
import gift.product.service.OptionService;
import gift.user.model.dto.AppUser;
import gift.wishlist.service.WishListService;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OptionService optionService;
    private final OrderRepository orderRepository;
    private final WishListService wishListService;

    public OrderService(OptionService optionService, OrderRepository orderRepository, WishListService wishListService) {
        this.optionService = optionService;
        this.orderRepository = orderRepository;
        this.wishListService = wishListService;
    }

    public OrderResponse createOrder(AppUser user, OrderRequest orderRequest) {
        Option option = optionService.subtractOptionQuantity(orderRequest.optionId(), orderRequest.quantity());
        Order order = new Order(option, user, orderRequest.quantity(), orderRequest.message());
        order = orderRepository.save(order);

        checkOrderInWishList(user, option);

        return new OrderResponse(order.getId(), order.getOption().getId(), order.getQuantity(),
                order.getRegistrationDate(), order.getMessage());
    }

    private void checkOrderInWishList(AppUser appUser, Option option) {
        Product product = option.getProduct();
        wishListService.deleteWishIfExists(appUser.getId(), product.getId());
    }
}
