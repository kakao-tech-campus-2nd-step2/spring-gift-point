package gift.jpa.repository;


import static org.assertj.core.api.Assertions.assertThat;

import gift.product.entity.Product;
import gift.product.repository.ProductJpaRepository;
import gift.user.repository.UserJpaRepository;
import gift.wish.dto.request.UpdateWishRequest;
import gift.wish.entity.Wish;
import gift.wish.repository.WishRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@DataJpaTest
@Sql(scripts = {"/sql/initialize.sql", "/sql/insert_categories.sql",
    "/sql/insert_products.sql", "/sql/insert_users.sql",
    "/sql/insert_wishes.sql"},
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class WishRepositoryTest {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private UserJpaRepository userRepository;

    @Autowired
    private ProductJpaRepository productRepository;

    @Test
    @DisplayName("user id를 통해 wishes 찾기")
    void findByUserId() {
        // given
        Product product1 = new Product(
            "Product A",
            1000,
            "http://example.com/images/product_a.jpg",
            null
        );
        Product product3 = new Product(
            "Product C",
            3000,
            "http://example.com/images/product_c.jpg",
            null
        );

        // when
        final Page<Wish> response = wishRepository.findByUserId(1L, Pageable.unpaged());
        List<Wish> actual = response.getContent();

        // then
        assertThat(actual).isNotEmpty();
        assertThat(actual.size()).isEqualTo(2);

        // product1, quantity: 2
        assertThat(actual.getFirst().getProduct().getName()).isEqualTo(product1.getName());
        assertThat(actual.getFirst().getProduct().getPrice()).isEqualTo(product1.getPrice());
        assertThat(actual.getFirst().getProduct().getImageUrl()).isEqualTo(product1.getImageUrl());
        assertThat(actual.getFirst().getQuantity()).isEqualTo(2);

        // product3, quantity: 1
        assertThat(actual.get(1).getProduct().getName()).isEqualTo(product3.getName());
        assertThat(actual.get(1).getProduct().getPrice()).isEqualTo(product3.getPrice());
        assertThat(actual.get(1).getProduct().getImageUrl()).isEqualTo(product3.getImageUrl());
        assertThat(actual.get(1).getQuantity()).isEqualTo(1);
    }

    @Test
    @DisplayName("새 위시 추가 테스트")
    void createTest() {
        // given
        Long userId = 1L;
        Long productId = 2L;
        Wish newWish = new Wish(
            userRepository.findById(userId).get(),
            productRepository.findById(productId).get(),
            2
        );

        // when
        final Wish actual = wishRepository.save(newWish);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getUser().getId()).isEqualTo(userId);
        assertThat(actual.getProduct().getId()).isEqualTo(productId);
        assertThat(actual.getQuantity()).isEqualTo(2);
    }

    @Test
    @DisplayName("wish update test")
    void updateWishTest() {
        // given
        UpdateWishRequest request = new UpdateWishRequest(1L, 1L, 50);

        // when
        final Wish wish = wishRepository.findById(1L).get();
        wish.changeQuantity(request);
        final Wish actual = wishRepository.findById(1L).get();

        // then
        assertThat(actual.getQuantity()).isEqualTo(request.quantity());
    }

    @Test
    @DisplayName("삭제 테스트")
    void deleteTest() {
        // given
        final Wish actual = wishRepository.findByUserId(1L, Pageable.unpaged()).getContent().getFirst();
        Long actualId = actual.getId();

        // when
        wishRepository.delete(actual);

        // then
        assertThat(wishRepository.findById(actualId)).isNotPresent();
    }


}
