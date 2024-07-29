package gift.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WishTest {

    @Test
    void 위시_생성_성공() {
        User user = new User("test@example.com", "password123");
        Product product = new Product(new ProductName("테스트 상품"), 1000, "https://example.com/image.jpg", new Category("테스트 카테고리", "#FF0000", "https://example.com/test.png", "테스트 설명"));

        Wish wish = new Wish(user, product);

        assertNotNull(wish);
        assertEquals(user, wish.getUser());
        assertEquals(product, wish.getProduct());
    }
}
