package gift.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import gift.common.dto.PageResponse;
import gift.controller.wish.dto.WishRequest.Create;
import gift.controller.wish.dto.WishResponse;
import gift.model.Category;
import gift.model.Product;
import gift.model.User;
import gift.model.Wish;
import gift.repository.ProductRepository;
import gift.repository.UserRepository;
import gift.repository.WishRepository;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

@ExtendWith(MockitoExtension.class)
@Sql("/truncate.sql")
public class WishServiceTest {

    @Mock
    private WishRepository wishRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WishService wishService;

    @Test
    @DisplayName("위시 리스트 등록")
    void register() {
        User user = new User(1L, "yso3865", "yso8296@gmail.com");
        Category category = new Category(null, "상품", "red", "https://st.kakaocdn.net/category1.jpg",
            "상품 카테고리입니다.");
        Product product = new Product(1L, "product1", 1000, "product.jpg", category);
        Create request = new Create(1L, 3);
        Wish wish = new Wish(1L, user, product, 3);  // ID를 1L로 설정합니다.

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(productRepository.findById(1L)).willReturn(Optional.of(product));
        given(wishRepository.existsByProductIdAndUserId(1L, 1L)).willReturn(false);
        given(wishRepository.save(any())).willReturn(wish);

        // When
        Long id = wishService.addWistList(1L, request);

        // Then
        assertThat(id).isEqualTo(1L);
    }

    @Test
    @DisplayName("위시 리스트 조회")
    void findWish() {
        User user = new User(1L, "yso3865", "yso8296@gmail.com");
        Category category = new Category(1L, "상품", "red", "https://st.kakaocdn.net/category1.jpg",
            "상품 카테고리입니다.");
        Product product = new Product(1L, "product1", 1000, "image1.jpg", category);
        Wish wish = new Wish(1L, user, product, 3);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Wish> wishPage = new PageImpl<>(Collections.singletonList(wish), pageable, 1);
        given(wishRepository.findByUserId(1L, pageable)).willReturn(wishPage);

        PageResponse<WishResponse> wishes = wishService.findAllWish(1L, pageable);

        assertThat(wishes).isNotNull();
        assertThat(wishes.responses()).hasSize(1);
        assertThat(wishes.responses().get(0).productName()).isEqualTo("product1");
        then(wishRepository).should().findByUserId(1L, pageable);
    }

    @Test
    @DisplayName("위시 리스트 삭제")
    void delete() {
        User user = new User(1L, "yso3865", "yso8296@gmail.com");
        Category category = new Category(1L, "상품", "red", "https://st.kakaocdn.net/category1.jpg",
            "상품 카테고리입니다.");
        Product product = new Product(1L, "product1", 1000, "image1.jpg", category);
        Wish wish = new Wish(1L, user, product, 3);

        given(wishRepository.findById(any())).willReturn(Optional.of(wish));

        wishService.deleteWishList(1L, 1L);

        then(wishRepository).should().findById(any());
    }
}
