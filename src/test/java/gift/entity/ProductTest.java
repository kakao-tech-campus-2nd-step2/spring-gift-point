package gift.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gift.exception.MinimumOptionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    private Category category;
    private Product product;

    @BeforeEach
    void setUp() {
        category = new Category("음식", "testColor", "testImage.jpg", "TestDescription");
        product = new Product("상품", 10000, "testImg.jpg", category);
    }

    @Test
    @DisplayName("생성자 테스트")
    void ProductConstructorTest() {
        Product newProduct = new Product("전자제품", 20000, "testImg2.jpg", category);

        assertThat(newProduct).isNotNull();
        assertThat(newProduct.getName()).isEqualTo("전자제품");
        assertThat(newProduct.getPrice()).isEqualTo(20000);
        assertThat(newProduct.getImageUrl()).isEqualTo("testImg2.jpg");
        assertThat(newProduct.getCategory()).isEqualTo(category);
    }

    @Test
    @DisplayName("Getter 테스트")
    void ProductGetterTest() {
        assertThat(product.getName()).isEqualTo("상품");
        assertThat(product.getPrice()).isEqualTo(10000);
        assertThat(product.getImageUrl()).isEqualTo("testImg.jpg");
        assertThat(product.getCategory()).isEqualTo(category);
    }

    @Test
    @DisplayName("productUpdate 테스트")
    void productUpdateTest() {
        product.updateProduct("자동차", 35000000, "자동차 이미지", category);

        assertThat(product.getName()).isEqualTo("자동차");
        assertThat(product.getPrice()).isEqualTo(35000000);
        assertThat(product.getImageUrl()).isEqualTo("자동차 이미지");
        assertThat(product.getCategory()).isEqualTo(category);
    }

    @Test
    @DisplayName("removeOption 테스트")
    void removeOptionTest() {
        Option option1 = new Option("옵션", 300, product);
        Option option2 = new Option("옵션2", 300, product);
        product.getOptions().add(option1);
        product.getOptions().add(option2);

        assertThat(product.getOptions().get(0).getName()).isEqualTo("옵션");

        product.removeOption(option1, product);

        assertThat(product.getOptions().get(0).getName()).isEqualTo("옵션2");

        assertThatThrownBy(() -> product.removeOption(option2, product))
            .hasMessage("상품의 옵션이 1개 이하인 경우 옵션을 삭제할 수 없습니다.")
            .isInstanceOf(MinimumOptionException.class);
    }

    @Test
    @DisplayName("optionSize 테스트")
    void optionSizeTest() {
        Option option = new Option("옵션", 300, product);
        product.getOptions().add(option);

        assertThat(product.optionAmount()).isEqualTo(1);
    }
}
