package gift.EntityTest;

import gift.Model.Entity.Category;
import gift.Model.Entity.Option;
import gift.Model.Entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OptionTest {

    private Product product1;

    @BeforeEach
    void beforEach(){
        Category category = new Category("식품", "#812f3D", "식품 url", "");
        product1 = new Product("아메리카노", 4000, "아메리카노url", category);
    }

    @Test
    void creationTest(){
        Option option = new Option("옵션1", 1, product1);

        assertAll(
                ()->assertThat(option.getName().getValue()).isEqualTo("옵션1"),
                ()->assertThat(option.getQuantity().getValue()).isEqualTo(1),
                ()->assertThat(option.getProduct()).isEqualTo(product1)
        );
    }

    @Test
    void updateTest(){
        Option option = new Option("옵션1", 1, product1);
        option.update("옵션2", 2);

        assertAll(
                () -> assertThat(option.getName().getValue()).isEqualTo("옵션2"),
                () -> assertThat(option.getQuantity().getValue()).isEqualTo(2)
        );
    }

    @Test
    void subtractTest(){
        Option option = new Option("옵션1", 5, product1);
        option.subtractQuantity(3);

        assertThat(option.getQuantity().getValue()).isEqualTo(2);
    }
}
