package gift.domain;

import gift.model.category.Category;
import gift.model.option.Option;
import gift.model.product.Product;
import gift.model.user.User;
import gift.model.wishlist.WishList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WishListTest {

    @Test
    @DisplayName("위시리스트 생성 테스트")
    void createWish() {
        // given
        User user = createUser("kakao", "kakao@kakao.com", "passowrd", "user");
        Product product = createProduct("Product", 1000, "image", null);
        int quantity = 2;

        // when
        WishList wishList = createWishList(user, product, quantity);

        // then
        Assertions.assertThat(wishList.getUser()).isEqualTo(user);
        Assertions.assertThat(wishList.getProduct()).isEqualTo(product);
        Assertions.assertThat(wishList.getQuantity()).isEqualTo(quantity);

    }

    @Test
    @DisplayName("위시리스트 수량 수정 테스트")
    void updateWishList() {
        // given
        User user = createUser("kakao", "kakao@kakao.com", "passowrd", "user");
        Product product = createProduct("Product", 1000, "image", null);
        WishList wishList = createWishList(user, product, 5);
        int newQuantity = 10;

        // when
        wishList.update(newQuantity);

        // then
        Assertions.assertThat(wishList.getQuantity()).isEqualTo(newQuantity);
    }
    private User createUser(String name, String email, String password, String role) {
        return new User(name, email, password, role);
    }

    private Product createProduct(String name, int price, String url, Category category) {
        return new Product(name, price, url, category);
    }

    private WishList createWishList(User user, Product product, int quantity) {
        return new WishList(user, product, quantity);
    }
}
