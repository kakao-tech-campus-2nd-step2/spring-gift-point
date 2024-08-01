package gift.payment.application;

import gift.product.domain.Product;
import gift.product.domain.ProductOption;
import gift.product.domain.WishList;
import gift.product.domain.WishListProduct;
import gift.product.exception.ProductException;
import gift.product.infra.ProductRepository;
import gift.product.infra.WishListRepository;
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

    public PaymentService(WishListRepository wishListRepository, ProductRepository productRepository, PaymentEventService paymentEventService) {
        this.wishListRepository = wishListRepository;
        this.productRepository = productRepository;
        this.paymentEventService = paymentEventService;
    }

    @Transactional
    public void processPayment(Long userId, Long wishListId) {
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
