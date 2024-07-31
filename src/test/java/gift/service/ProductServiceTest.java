package gift.service;

import gift.dto.option.OptionRequest;
import gift.dto.product.ProductAddRequest;
import gift.dto.product.ProductUpdateRequest;
import gift.exception.InvalidProductNameWithKAKAOException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("정상 상품 추가하기")
    void successAddProduct() {
        //given
        var optionRequest = new OptionRequest("옵션", 1000);
        var options = new ArrayList<OptionRequest>();
        options.add(optionRequest);
        var productRequest = new ProductAddRequest("상품1", 10000, "이미지 주소", 1L, options);
        //when
        var savedProduct = productService.addProduct(productRequest);
        //then
        Assertions.assertThat(savedProduct.name()).isEqualTo("상품1");

        productService.deleteProduct(savedProduct.id());
    }

    @Test
    @DisplayName("이용자로 카카오가 포함된 상품 추가하기")
    void failAddProductWithNameKakao() {
        //given
        var optionRequest = new OptionRequest("옵션", 1000);
        var options = new ArrayList<OptionRequest>();
        options.add(optionRequest);
        var productRequest = new ProductAddRequest("카카오상품", 10000, "이미지 주소", 1L, options);
        //when, then
        Assertions.assertThatThrownBy(() -> productService.addProduct(productRequest))
                .isInstanceOf(InvalidProductNameWithKAKAOException.class);
    }

    @Test
    @DisplayName("상품 수정하기")
    void successUpdateProduct() {
        //given
        var optionRequest = new OptionRequest("옵션", 1000);
        var options = new ArrayList<OptionRequest>();
        options.add(optionRequest);
        var productRequest = new ProductAddRequest("상품1", 10000, "이미지 주소", 1L, options);
        var savedProduct = productService.addProduct(productRequest);
        var id = savedProduct.id();
        var updateDto = new ProductUpdateRequest("상품1", 7000, "이미지 주소2", 1L);
        //when
        productService.updateProduct(id, updateDto);
        //then
        var updatedProduct = productService.getProduct(id);
        Assertions.assertThat(updatedProduct.price()).isEqualTo(7000);
        Assertions.assertThat(savedProduct.id()).isEqualTo(updatedProduct.id());

        productService.deleteProduct(id);
    }
}
