package gift.doamin;

import gift.domain.Category;
import gift.domain.Product;
import gift.domain.User;
import gift.domain.WishProduct;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class WishProductTest {
    @Test
    void wishTest(){
        Category category = new Category("물품");
        Product product = new Product("name", 4000,"none", category);
        User user = new User("bae", "Bae@email.com", "aaaa");
        WishProduct wishProduct = new WishProduct(user, product);
        Assertions.assertThat(wishProduct).isNotNull();

        Assertions.assertThat(wishProduct.getProduct()).isEqualTo(product);
        Assertions.assertThat(wishProduct.getUser()).isEqualTo(user);
    }
}
