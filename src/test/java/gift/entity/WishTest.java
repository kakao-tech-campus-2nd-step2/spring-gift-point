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
        wish = new Wish(user, product);
    }

    @Test
    @DisplayName("생성자 테스트")
    void WishConstructorTest() {
        Wish newWish = new Wish(user, product);

        assertThat(newWish).isNotNull();
        assertThat(newWish.getUser()).isEqualTo(user);
        assertThat(newWish.getProduct()).isEqualTo(product);
    }

    @Test
    @DisplayName("Getter 테스트")
    void WishGetterTest() {
        assertThat(wish.getUser()).isEqualTo(user);
        assertThat(wish.getProduct()).isEqualTo(product);
    }

    @Test
    @DisplayName("sameProduct 테스트")
    void sameProductTest() {
        Wish newWish = new Wish(user, product);

        assertThat(newWish.sameProduct(product)).isTrue();
    }

}
