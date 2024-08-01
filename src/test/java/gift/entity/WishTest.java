package gift.entity;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class WishTest {

    @Test
    void wishConstructorAndGetters() {
        Member member = new Member("test@example.com", "password");
        Category category = new Category("Test Category", "#FFFFFF", "http://example.com/test.jpg", "Description");
        Product product = new Product("Test Product", 100, "http://example.com/test.jpg", category);
        Wish wish = new Wish(member, product, 1);

        assertThat(wish.getMember()).isEqualTo(member);
        assertThat(wish.getProduct()).isEqualTo(product);
        assertThat(wish.getProductNumber()).isEqualTo(1);
    }

    @Test
    void wishSetters() {
        Member member = new Member("test@example.com", "password");
        Category category = new Category("Test Category", "#FFFFFF", "http://example.com/test.jpg", "Description");
        Product product = new Product("Test Product", 100, "http://example.com/test.jpg", category);
        Wish wish = new Wish();
        wish.setMember(member);
        wish.setProduct(product);
        wish.setProductNumber(2);

        assertThat(wish.getMember()).isEqualTo(member);
        assertThat(wish.getProduct()).isEqualTo(product);
        assertThat(wish.getProductNumber()).isEqualTo(2);
    }
}
