package gift.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    @DisplayName("상품 생성 테스트")
    void createProduct() {
        Category category = new Category("Category 1", "Red", "img1.jpg", "Description 1");
        Product product = new Product("Product 1", 100, "img1.jpg", category);

        assertThat(product.getName()).isEqualTo("Product 1");
        assertThat(product.getPrice()).isEqualTo(100);
        assertThat(product.getImgUrl()).isEqualTo("img1.jpg");
        assertThat(product.getCategory()).isEqualTo(category);
    }

    @Test
    @DisplayName("상품 정보 수정 테스트")
    void updateProduct() {
        Category category = new Category("Category 1", "Red", "img1.jpg", "Description 1");
        Product product = new Product("Product 1", 100, "img1.jpg", category);

        Category newCategory = new Category("Category 2", "Blue", "img2.jpg", "Description 2");
        product.update("Updated Product", 200, "updated_img.jpg", newCategory);

        assertThat(product.getName()).isEqualTo("Updated Product");
        assertThat(product.getPrice()).isEqualTo(200);
        assertThat(product.getImgUrl()).isEqualTo("updated_img.jpg");
        assertThat(product.getCategory()).isEqualTo(newCategory);
    }

    @Test
    @DisplayName("옵션 추가 테스트")
    void addOption() {
        Category category = new Category("Category 1", "Red", "img1.jpg", "Description 1");
        Product product = new Product("Product 1", 100, "img1.jpg", category);
        Option option = new Option("Option 1", product, 10);

        product.addOption(option);

        assertThat(product.getOptions()).contains(option);
        assertThat(option.getProduct()).isEqualTo(product);
    }

    @Test
    @DisplayName("옵션 제거 테스트")
    void removeOption() {
        Category category = new Category("Category 1", "Red", "img1.jpg", "Description 1");
        Product product = new Product("Product 1", 100, "img1.jpg", category);
        Option option = new Option("Option 1", product, 10);

        product.addOption(option);
        product.removeOption(option);

        assertThat(product.getOptions()).doesNotContain(option);
        assertThat(option.getProduct()).isNull();
    }
}