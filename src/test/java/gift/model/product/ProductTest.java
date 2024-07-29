package gift.model.product;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

public class ProductTest {

    @Test
    @DisplayName("Product 생성 테스트")
    void product_생성() {
        //given
        String name = "테스트 상품";
        Integer price = 10000;
        String imageUrl = "test.jpg";
        Category category = new Category("category", "ABCD", "test", "test");

        //when
        Product product = Product.create(null, name, price, imageUrl, category);

        // then
        assertAll(
            () -> assertThat(product.getName()).isEqualTo(name),
            () -> assertThat(product.getPrice()).isEqualTo(price),
            () -> assertThat(product.getImageUrl()).isEqualTo(imageUrl)
        );
    }

    @ParameterizedTest
    @DisplayName("Product 실패 이름 유효성 테스트")
    @ValueSource(strings = {"카카오를 포함하면 안됨", "15를_넘으면_제작에_실패해야_합니다", "특수문자()[]+-&/_만가능!"})
    void product_실패_이름_유효성(String testName) {
        //given

        Integer price = 10000;
        String imageUrl = "test.jpg";
        Category category = new Category("category", "ABCD", "test", "test");

        //when
        // then
        assertThrows(IllegalArgumentException.class, () -> {
            Product.create(null, testName, price, imageUrl, category);
        });
    }

    @Test
    @DisplayName("Product 성공 이름 유효성 테스트")
    void product_성공_이름_유효성() {
        //given
        String name = "특수문자()[]+-&/_가능";

        Integer price = 10000;
        String imageUrl = "test.jpg";
        Category category = new Category("category", "ABCD", "test", "test");

        //when
        Product product = Product.create(null, name, price, imageUrl, category);

        // then
        assertAll(
            () -> assertThat(product.getName()).isEqualTo(name),
            () -> assertThat(product.getPrice()).isEqualTo(price),
            () -> assertThat(product.getImageUrl()).isEqualTo(imageUrl)
        );
    }

}
