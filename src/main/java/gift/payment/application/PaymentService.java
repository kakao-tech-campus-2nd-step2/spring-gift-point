package gift.payment.application;

import gift.order.domain.OrderCreateResponse;
import gift.order.service.OrderService;
import gift.payment.domain.PaymentRequest;
import gift.payment.domain.PaymentResponse;
import gift.product.domain.Product;
import gift.product.domain.ProductOption;
import gift.product.domain.WishList;
import gift.product.domain.WishListProduct;
import gift.product.exception.ProductException;
import gift.product.infra.ProductRepository;
import gift.product.infra.WishListRepository;
import gift.user.application.PointService;
import gift.util.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class PaymentService {

    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;
    private final PaymentEventService paymentEventService;
    private final PointService pointService;
    private final OrderService orderService;

    public PaymentService(WishListRepository wishListRepository, ProductRepository productRepository, PaymentEventService paymentEventService, PointService pointService, OrderService orderService) {
        this.wishListRepository = wishListRepository;
        this.productRepository = productRepository;
        this.paymentEventService = paymentEventService;
        this.orderService = orderService;
        this.pointService = pointService;
    }

    @Transactional
    public void processPaymentByWishListId(Long userId, Long wishListId) {
        WishList wishList = wishListRepository.findById(wishListId);

        if (!Objects.equals(wishList.getUser().getId(), userId)) {
            throw new ProductException(ErrorCode.NOT_USER_OWNED);
        }

        WishListProduct product = wishList.getWishListProduct();
        Product wishListProduct = product.getProduct();
        ProductOption productOption = productRepository.getProductWithOption(wishListProduct.getId(), product.getId());
        productOption.decreaseQuantity(product.getQuantity());

        productRepository.save(wishListProduct);
        wishListRepository.delete(wishList);
    }

    @Transactional
    public PaymentResponse processPayment(Long userId, PaymentRequest request) {
        // validate 1. 상품 존재 여부
        Product product = productRepository.findProductOptionsByProductId(request.getProductId())
                .stream()
                .map(ProductOption::getProduct)
                .findFirst()
                .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));

        // 옵션 재고 확인
        ProductOption productOption = productRepository.getProductWithOption(request.getProductId(), request.getOptionId());
        if (productOption.getQuantity() < request.getQuantity()) {
            throw new ProductException(ErrorCode.OUT_OF_STOCK);
        }

        // 포인트 사용
        pointService.userPoint(userId, request.getPoint());

        // 재고 개수 줄이기
        productRepository.decreaseOptionQuantity(request.getOptionId(), request.getQuantity());

        // 주문 생성
        PaymentResponse order = orderService.createOrder(userId, request);

        return order;
    }

    public Long calcPayment(Long optionId, Long productId, Long quantity) {
        List<ProductOption> productOptionsByProductId = productRepository.findProductOptionsByProductId(productId);
        ProductOption productOption = productOptionsByProductId.stream()
                .filter(option -> option.getId().equals(optionId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 상품 옵션을 찾을 수 없습니다."));

        Product product = productRepository.findById(productId);

        Long price = product.getPrice() * quantity;
        return paymentEventService.일정금액_이상이면_할인이벤트(price);
    }
}
