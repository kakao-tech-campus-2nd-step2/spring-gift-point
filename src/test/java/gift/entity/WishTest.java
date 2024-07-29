package gift.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WishTest {

    private User user;
    private Product product;
    private Wish wish;

    @BeforeEach
    void setUp() {
        user = new User("test@example.com", "password123");
        product = new Product("상품", 10000, "testImg.jpg",
            new Category("음식", "testColor", "testImage.jpg", "TestDescription"));
        wish = new Wish(user, product, 1);
    }

    @Test
    @DisplayName("생성자 테스트")
    void WishConstructorTest() {
        Wish newWish = new Wish(user, product, 2);

        assertThat(newWish).isNotNull();
        assertThat(newWish.getUser()).isEqualTo(user);
        assertThat(newWish.getProduct()).isEqualTo(product);
        assertThat(newWish.getNumber()).isEqualTo(2);
    }

    @Test
    @DisplayName("Getter 테스트")
    void WishGetterTest() {
        assertThat(wish.getUser()).isEqualTo(user);
        assertThat(wish.getProduct()).isEqualTo(product);
        assertThat(wish.getNumber()).isEqualTo(1);
    }

    @Test
    @DisplayName("subtractNumber 테스트")
    void subtractNumberTest() {

        assertThat(wish.getNumber()).isEqualTo(1);

        wish.subtractNumber(1);

        assertThat(wish.getNumber()).isEqualTo(0);
    }

    @Test
    @DisplayName("sameProduct 테스트")
    void sameProductTest() {
        Wish newWish = new Wish(user, product, 3);

        assertThat(newWish.sameProduct(product)).isTrue();
    }

    @Test
    @DisplayName("checkLeftWishNumber 테스트")
    void checkLeftWishNumberTest() {

        assertThat(wish.checkLeftWishNumber()).isFalse();

        wish.subtractNumber(1);

        assertThat(wish.checkLeftWishNumber()).isTrue();
    }
}
