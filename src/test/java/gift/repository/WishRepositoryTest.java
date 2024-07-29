package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.domain.Category;
import gift.domain.Product;
import gift.domain.UserInfo;
import gift.domain.Wish;
import gift.utils.error.WishListNotFoundException;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
public class WishRepositoryTest {

    @Autowired
    private WishRepository wishRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private Category createAndSaveCategory() {
        Category category = new Category("Test Category", "Test Description", "Test Color", "Test Image URL");
        return categoryRepository.save(category);
    }

    @Test
    @DisplayName("userId로 Wish 찾기 테스트")
    void findByUserId() {
        Category category = createAndSaveCategory();
        Product product = new Product("original", 1000, "img", category);
        UserInfo userInfo = new UserInfo("kakaocampus@gmail.com", "kakao2024");
        productRepository.save(product);
        userInfoRepository.save(userInfo);

        Wish wish = new Wish(product, userInfo, 1L);
        wishRepository.save(wish);
        int pageSize = 10;
        Pageable pageable = PageRequest.of(0, pageSize);

        Page<Wish> byUserId = wishRepository.findByUserInfoId(userInfo.getId(), pageable);

        assertThat(byUserId).isNotEmpty();
        assertThat(byUserId.getContent().get(0))
            .extracting(Wish::getUserInfo, Wish::getProduct, Wish::getQuantity)
            .containsExactly(userInfo, product, 1L);

        assertThat(byUserId.getSize()).isEqualTo(pageSize);
        assertThat(byUserId.getTotalElements()).isEqualTo(1);
        assertThat(byUserId.getTotalPages()).isEqualTo(1);
        assertThat(byUserId.getNumber()).isZero();
    }

    @Test
    @DisplayName("위시 아이템 수량 업데이트")
    void updateWishQuantity() {
        Category category = createAndSaveCategory();
        Product product = new Product("pak", 1000, "jpg", category);
        UserInfo userInfo = new UserInfo("kakaocampus@gmail.com", "kakao2024");
        productRepository.save(product);
        userInfoRepository.save(userInfo);
        Wish wish = new Wish(product, userInfo, 1L);
        Wish savedWish = wishRepository.save(wish);

        savedWish.setQuantity(5L);
        wishRepository.save(savedWish);

        Optional<Wish> updatedWishOptional = wishRepository.findByUserInfoIdAndProductId(userInfo.getId(), product.getId());
        assertThat(updatedWishOptional).isPresent();
        assertThat(updatedWishOptional.get().getQuantity()).isEqualTo(5L);
    }

    @Test
    @DisplayName("존재하지 않는 위시 아이템 조회")
    void findNonExistentWish() {
        Optional<Wish> nonExistentWish = wishRepository.findByUserInfoIdAndProductId(999L, 999L);
        assertThat(nonExistentWish).isEmpty();
    }

    @Test
    @DisplayName("여러 사용자의 위시 리스트 테스트")
    void multipleUserWishLists() {
        Category category = createAndSaveCategory();
        Product product1 = new Product("pak", 1000, "jpg", category);
        Product product2 = new Product("jeong", 2000, "png", category);
        Product product3 = new Product("woo", 3000, "gif", category);
        UserInfo userInfo1 = new UserInfo("kakaocampus@gmail.com", "kakao2024");
        UserInfo userInfo2 = new UserInfo("kakao@gmail.com", "kakao");
        UserInfo userInfo3 = new UserInfo("campus@gmail.com", "2024");

        productRepository.saveAll(Arrays.asList(product1, product2, product3));
        userInfoRepository.saveAll(Arrays.asList(userInfo1, userInfo2, userInfo3));
        Wish wish1 = new Wish(product1, userInfo1, 1L);
        Wish wish2 = new Wish(product2, userInfo2, 2L);
        Wish wish3 = new Wish(product3, userInfo3, 3L);

        wishRepository.saveAll(Arrays.asList(wish1, wish2, wish3));

        Pageable pageable = PageRequest.of(0, 10);

        Page<Wish> wishesForUser1 = wishRepository.findByUserInfoId(userInfo1.getId(), pageable);
        Page<Wish> wishesForUser2 = wishRepository.findByUserInfoId(userInfo2.getId(), pageable);
        Page<Wish> wishesForUser3 = wishRepository.findByUserInfoId(userInfo3.getId(), pageable);

        assertThat(wishesForUser1.getContent()).hasSize(1);
        assertThat(wishesForUser1.getContent().get(0).getQuantity()).isEqualTo(1L);
        assertThat(wishesForUser2.getContent()).hasSize(1);
        assertThat(wishesForUser2.getContent().get(0).getQuantity()).isEqualTo(2L);
        assertThat(wishesForUser3.getContent()).hasSize(1);
        assertThat(wishesForUser3.getContent().get(0).getQuantity()).isEqualTo(3L);

        assertThat(wishRepository.findAll()).hasSize(3);
    }

    @Test
    @DisplayName("유저아이디와 제품아이디로 위시리스트 삭제하기,존재하는지 확인하기")
    void deleteByProductIdAndUserInfoId() {
        Category category = createAndSaveCategory();
        Product product = new Product("original", 1000, "img", category);
        UserInfo userInfo = new UserInfo("kakaocampus@gmail.com", "kakao2024");
        productRepository.save(product);
        userInfoRepository.save(userInfo);
        Wish wish = new Wish(product, userInfo, 1L);
        wishRepository.save(wish);

        assertThat(wishRepository.existsByUserInfoIdAndProductId(userInfo.getId(), product.getId())).isTrue();

        wishRepository.deleteByProductIdAndUserInfoId(product.getId(), userInfo.getId());

        assertThat(wishRepository.existsByUserInfoIdAndProductId(userInfo.getId(), product.getId())).isFalse();
        assertThat(wishRepository.findByUserInfoIdAndProductId(userInfo.getId(), product.getId())).isEmpty();
    }

    @Test
    @DisplayName("유저아이디와 제품아이디로 위시리스트 찾기")
    void findByUserInfoIdAndProductId() {
        Category category = createAndSaveCategory();
        Product product = new Product("original", 1000, "img", category);
        UserInfo userInfo = new UserInfo("kakaocampus@gmail.com", "kakao2024");
        productRepository.save(product);
        userInfoRepository.save(userInfo);
        Wish wish = new Wish(product, userInfo, 1L);
        wishRepository.save(wish);

        Optional<Wish> foundWish = wishRepository.findByUserInfoIdAndProductId(userInfo.getId(), product.getId());

        assertThat(foundWish).isPresent();
        assertThat(foundWish.get())
            .extracting(Wish::getProduct, Wish::getUserInfo, Wish::getQuantity)
            .containsExactly(product, userInfo, 1L);
    }
}
