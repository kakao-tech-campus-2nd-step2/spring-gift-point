package gift.init;

import gift.init.auth.UserCreator;
import gift.init.point.DiscountPolicyCreator;
import gift.init.product.CategoryCreator;
import gift.init.product.ProductCreator;
import gift.init.product.ProductOptionCreator;
import gift.init.product.ProductOrderCreator;
import gift.init.product.WishCreator;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class InitData {

    private final UserCreator userCreator;
    private final CategoryCreator categoryCreator;
    private final ProductCreator productCreator;
    private final WishCreator wishCreator;
    private final ProductOptionCreator productOptionCreator;
    private final ProductOrderCreator productOrderCreator;
    private final DiscountPolicyCreator discountPolicyCreator;

    @Autowired
    public InitData(UserCreator userCreator, CategoryCreator categoryCreator,
        ProductCreator productCreator, WishCreator wishCreator,
        ProductOptionCreator productOptionCreator,
        ProductOrderCreator productOrderCreator,
        DiscountPolicyCreator discountPolicyCreator) {
        this.userCreator = userCreator;
        this.categoryCreator = categoryCreator;
        this.productCreator = productCreator;
        this.wishCreator = wishCreator;
        this.productOptionCreator = productOptionCreator;
        this.productOrderCreator = productOrderCreator;
        this.discountPolicyCreator = discountPolicyCreator;
    }

    @PostConstruct
    public void init() {
        //        유저 생성
        userCreator.creator();
        //        카테고리 생성
        categoryCreator.creator();
        //        상품 생성
        productCreator.creator();
        //        위시리스트 생성
        wishCreator.creator();
        //        상품의 옵션 추가
        productOptionCreator.creator();
        //        상품의 옵션 재고 증감(상품주문)
        productOrderCreator.creator();
        //        할인정책 추가
        discountPolicyCreator.creator();
    }
}
