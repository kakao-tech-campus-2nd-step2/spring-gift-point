package gift.service;

import gift.domain.Option;
import gift.domain.Order;
import gift.domain.Product;
import gift.dto.request.OrderRequest;
import gift.dto.request.PriceRequest;
import gift.dto.response.OrderResponse;
import gift.dto.response.PriceResponse;
import gift.exception.CategoryNotFoundException;
import gift.exception.OptionNotFoundException;
import gift.exception.OptionNotMatchProductException;
import gift.exception.ProductNotFoundException;
import gift.repository.order.OrderSpringDataJpaRepository;
import gift.repository.product.ProductSpringDataJpaRepository;
import gift.repository.wishlist.WishlistSpringDataJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static gift.exception.ErrorCode.*;


@Service
@Transactional
public class OrderService {

    private final OrderSpringDataJpaRepository orderRepository;
    private final ProductSpringDataJpaRepository productRepository;
    private final WishlistSpringDataJpaRepository wishlistRepository;

    private final OptionService optionService;
    private final KakaoMessageService kakaoMessageService;

    @Autowired
    public OrderService(OrderSpringDataJpaRepository orderRepository, ProductSpringDataJpaRepository productRepository, WishlistSpringDataJpaRepository wishlistRepository, OptionService optionService, KakaoMessageService kakaoMessageService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.wishlistRepository = wishlistRepository;
        this.optionService = optionService;
        this.kakaoMessageService = kakaoMessageService;
    }

    public OrderResponse createOrder(String token, OrderRequest orderRequest) {
        Long optionId = orderRequest.getOptionId();
        Integer quantity = orderRequest.getQuantity();
        Long productId = optionService.getProductIdByOptionId(optionId);

        optionService.subtractOptionQuantity(productId, optionId, quantity);

        wishlistRepository.deleteByMemberIdAndProductId(orderRequest.getReceiveMemberId(), productId);

        Order order = new Order(optionId, quantity, LocalDateTime.now(), orderRequest.getMessage(),orderRequest.getReceiveMemberId());

        orderRepository.save(order);

        kakaoMessageService.sendOrderMessage(token, order);

        return OrderResponse.fromOrder(order);
    }

    public PriceResponse getOrderPrice(PriceRequest priceRequest){
        Product product = productRepository.findById(priceRequest.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));

        Long productId = optionService.getProductIdByOptionId(priceRequest.getOptionId());
        if (!product.getId().equals(productId)) {
            throw new OptionNotMatchProductException(OPTION_NOT_MATCH_PRODUCT);
        }

        int orderPrice = product.getPrice() * priceRequest.getQuantity();

        if (orderPrice >= 50000) {
            orderPrice = (int) (orderPrice * 0.9);
        }

        return new PriceResponse(orderPrice);
    }
}
