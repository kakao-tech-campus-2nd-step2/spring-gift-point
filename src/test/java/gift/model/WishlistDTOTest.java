package gift.model;

import gift.model.product.Product;
import gift.model.wishlist.WishlistDTO;
import gift.repository.ProductRepository;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class WishlistDTOTest {

    private WishlistDTO wishlist;
    private Optional<Product> product;

    @Autowired
    private Validator validator;
    @MockBean
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        wishlist = new WishlistDTO(-1L);
    }

    @Test
    void 위시리스트에_추가하고_싶은데_실제로_존재하는_상품인_경우() {
        // given
        given(productRepository.findById(any())).willReturn(Optional.of(new Product()));

        // when
        var validate = validator.validate(wishlist);

        // then
        assertThat(validate).isEmpty();
    }

    @Test
    void 위시리스트에_추가하고_싶은데_실제로_존재하지_않는_상품인_경우() {
        // given
        given(productRepository.findById(any())).willReturn(Optional.empty());

        // when
        var validate = validator.validate(wishlist);

        // then
        assertThat(validate).isNotEmpty();
    }
}
