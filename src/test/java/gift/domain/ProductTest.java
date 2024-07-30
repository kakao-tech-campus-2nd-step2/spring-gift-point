package gift.domain;

import gift.common.exception.AlreadyExistName;
import gift.model.category.Category;
import gift.model.option.Option;
import gift.model.product.Product;
import gift.model.product.ProductRequest;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class ProductTest {
    @Test
    @DisplayName("상품 객체 생성 테스트")
    void create() {
        // given
        Category category = createCategory("카테고리", "blue", "image", "description");
        Option option = createOption("Option", 100, null);
        String name = "아메리카노";
        int price = 4500;
        String imageUrl = "americano";

        // when
        Product product = createProduct(name, price, imageUrl, category);


        // then
        Assertions.assertThat(product.getName()).isEqualTo(name);
        Assertions.assertThat(product.getImageUrl()).isEqualTo(imageUrl);
    }

    @Test
    @DisplayName("상품 수정 테스트")
    void updateProduct() {
        // given
        Category category = createCategory("카테고리", "blue", "image", "description");
        Option option = createOption("Option", 100, null);
        Product product = createProduct("아메리카노", 4500, "americano", category);
        ProductRequest request = new ProductRequest("카페라떼", 5000, "caffelatte", category.getId());

        // when
        product.update(request, category);

        // then
        Assertions.assertThat(product.getName()).isEqualTo(request.name());
        Assertions.assertThat(product.getImageUrl()).isEqualTo(request.imageUrl());
    }

    @DisplayName("상품에 옵션 추가 시 이름 중복 검증 테스트")
    @Test
    void validOptionName() {
        // given
        Category category = createCategory("카테고리", "blue", "image", "description");
        Product product = createProduct("Americano", 4500, "americano", category);
        Option option = createOption("Option", 100, product);
        product.addOption("Option", 50);

        // when, then
        org.junit.jupiter.api.Assertions.assertThrows(AlreadyExistName.class, () ->
            product.addOption("Option", 100));
    }


    private Product createProduct(String name, int price, String url, Category category) {
        return new Product(name, price, url, category);
    }

    private Category createCategory(String name, String color, String imageUrl, String description) {
        return new Category(name, color, imageUrl, description);
    }

    private Option createOption(String name, int quantity, Product product) {
        return new Option(name, quantity, product);
    }

}
