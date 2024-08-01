package gift.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WishTest {

    @Test
    public void testWishCreation() {
        Member member = new Member("test@example.com", "password123");
        Category category = new Category("Test Category");
        Product product = new Product("Test Product", 1000, "test.jpg", category);
        Wish wish = new Wish(member, product);

        assertEquals(member, wish.getMember());
        assertEquals(product, wish.getProduct());
    }
}
