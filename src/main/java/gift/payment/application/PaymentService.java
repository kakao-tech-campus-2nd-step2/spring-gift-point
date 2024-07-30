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

import java.util.Objects;

@Service
public class PaymentService {

    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;

    public PaymentService(WishListRepository wishListRepository, ProductRepository productRepository) {
        this.wishListRepository = wishListRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void processPayment(Long userId, Long wishListId) {
        WishList wishList = wishListRepository.findById(wishListId);

        if (!Objects.equals(wishList.getUser().getId(), userId)) {
            throw new ProductException(ErrorCode.NOT_USER_OWNED);
        }

        for (WishListProduct product : wishList.getWishListProducts()) {
            Product wishListProduct = product.getProduct();
            ProductOption productOption = product.getProductOption();
            productOption.decreaseQuantity(product.getQuantity());

            productRepository.save(wishListProduct);
        }
        wishListRepository.delete(wishList);
    }
}
