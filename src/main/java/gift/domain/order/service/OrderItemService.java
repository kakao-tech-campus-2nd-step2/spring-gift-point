package gift.domain.order.service;

import gift.domain.member.entity.Member;
import gift.domain.order.dto.OrderItemRequest;
import gift.domain.order.entity.Order;
import gift.domain.order.entity.OrderItem;
import gift.domain.product.entity.Option;
import gift.domain.product.entity.Product;
import gift.domain.product.repository.ProductJpaRepository;
import gift.domain.product.service.OptionService;
import gift.domain.wishlist.repository.WishlistJpaRepository;
import gift.exception.InvalidOptionInfoException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderItemService {

    private final ProductJpaRepository productJpaRepository;
    private final WishlistJpaRepository wishlistJpaRepository;
    private final OptionService optionService;

    public OrderItemService(
        ProductJpaRepository productJpaRepository,
        WishlistJpaRepository wishlistJpaRepository,
        OptionService optionService
    ) {
        this.productJpaRepository = productJpaRepository;
        this.wishlistJpaRepository = wishlistJpaRepository;
        this.optionService = optionService;
    }

    @Transactional
    public void createMultiple(Member member, Order order, List<OrderItemRequest> orderItemRequests) {
        for (OrderItemRequest orderItemRequest : orderItemRequests) {
            createOne(member, order, orderItemRequest);
        }
    }

    @Transactional
    public void createOne(Member member, Order order, OrderItemRequest orderItemRequest) {
        Long optionId = orderItemRequest.optionId();
        Product product = productJpaRepository.findByOptionId(optionId)
            .orElseThrow(() -> new InvalidOptionInfoException("error.invalid.option.id"));

        Option option = product.getOption(optionId);

        buy(optionId, orderItemRequest.quantity());

        OrderItem orderItem = orderItemRequest.toOrderItem(order, product, option);
        order.addOrderItem(orderItem);

        wishlistJpaRepository.deleteByMemberAndProduct(member, product);

        order.addOriginalPrice(product.getPrice() * orderItemRequest.quantity());
    }

    private void buy(Long optionId, int quantity) {
        optionService.subtractQuantity(optionId, quantity);
    }
}
