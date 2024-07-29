package gift.EntityTest;

import gift.Model.Entity.Category;
import gift.Model.Entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ProductTest {

    private Category category;
    private Category category2;

    @BeforeEach
    void beforeEach(){
        category = new Category("식품", "#812f3D", "식품 url", "");
        category2 = new Category("음료", "#732d2b", "음료 url", "");
    }

    @Test
    void creationTest() {
        Product product= new Product("아메리카노", 4000, "아메리카노url",category);
        assertAll(
                () -> assertThat(product.getName().getValue()).isEqualTo("아메리카노"),
                () -> assertThat(product.getPrice().getValue()).isEqualTo(4000),
                () -> assertThat(product.getImageUrl().getValue()).isEqualTo("아메리카노url"),
                ()-> assertThat(product.getCategory()).isEqualTo(category)
        );
    }

    @Test
    void setterTest() {
        Product product = new Product("아메리카노", 4000, "아메리카노url", category);
        product.update("카푸치노", 5000, "카푸치노url", category2);

        assertAll(
                () -> assertThat(product.getName().getValue()).isEqualTo("카푸치노"),
                () -> assertThat(product.getPrice().getValue()).isEqualTo(5000),
                () -> assertThat(product.getImageUrl().getValue()).isEqualTo("카푸치노url"),
                () -> assertThat(product.getCategory()).isEqualTo(category2)
        );
    }
}
