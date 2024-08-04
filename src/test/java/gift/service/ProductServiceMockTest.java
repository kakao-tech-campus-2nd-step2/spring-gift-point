package gift.service;

import gift.entity.middle.ProductOption;
import gift.entity.product.Product;
import gift.entity.user.User;
import gift.repository.ProductOptionRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class ProductServiceMockTest {

    @MockBean
    private ProductOptionRepository productOptionRepository;
    @MockBean
    private UserService userService;
    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    void 상품에_옵션이_한개_이하_존재할_때_옵션_삭제_불가() {
        // given
        User user = new User();
        user.setId(-1L);
        Product product = new Product();
        product.setUser(user);

        given(userService.findOne((String) any())).willReturn(user);
        given(productRepository.findById(any())).willReturn(Optional.of(product));

        given(productOptionRepository.findByProductId(any())).willReturn(List.of(new ProductOption()));

        // when
        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> productService.deleteProductOption(product.getId(), user.getId(), "test@naver.com"));
    }
}
