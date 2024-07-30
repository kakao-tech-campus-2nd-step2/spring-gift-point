package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.domain.Category;
import gift.domain.Product;
import gift.domain.UserInfo;
import gift.domain.Wish;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
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

    private Category category;
    private Product product1, product2;
    private UserInfo userInfo1, userInfo2;
    private Wish wish1, wish2;

    @BeforeEach
    void setup() {
        category = categoryRepository.save(new Category("Test Category", "Test Description", "Test Color", "Test Image URL"));

        product1 = productRepository.save(new Product("Product1", 1000, "img1", category));
        product2 = productRepository.save(new Product("Product2", 2000, "img2", category));

        userInfo1 = userInfoRepository.save(new UserInfo("user1@example.com", "password1"));
        userInfo2 = userInfoRepository.save(new UserInfo("user2@example.com", "password2"));

        wish1 = wishRepository.save(new Wish(product1, userInfo1, 1));
        wish2 = wishRepository.save(new Wish(product2, userInfo2, 2));
    }

    @Test
    @DisplayName("deleteByProductIdAndUserInfoId 테스트")
    void deleteByProductIdAndUserInfoId() {
        wishRepository.deleteByProductIdAndUserInfoId(product1.getId(), userInfo1.getId());
        assertThat(wishRepository.findByUserInfoIdAndProductId(userInfo1.getId(), product1.getId())).isEmpty();
    }

    @Test
    @DisplayName("findByUserInfoId 테스트")
    void findByUserInfoId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Wish> wishes = wishRepository.findByUserInfoId(userInfo1.getId(), pageable);

        assertThat(wishes.getContent()).hasSize(1);
        assertThat(wishes.getContent().get(0).getUserInfo()).isEqualTo(userInfo1);
    }

    @Test
    @DisplayName("existsByUserInfoIdAndProductId 테스트")
    void existsByUserInfoIdAndProductId() {
        assertThat(wishRepository.existsByUserInfoIdAndProductId(userInfo1.getId(), product1.getId())).isTrue();
        assertThat(wishRepository.existsByUserInfoIdAndProductId(userInfo1.getId(), product2.getId())).isFalse();
    }

    @Test
    @DisplayName("findByUserInfoIdAndProductId 테스트")
    void findByUserInfoIdAndProductId() {
        Optional<Wish> foundWish = wishRepository.findByUserInfoIdAndProductId(userInfo1.getId(), product1.getId());
        assertThat(foundWish).isPresent();
        assertThat(foundWish.get().getUserInfo()).isEqualTo(userInfo1);
        assertThat(foundWish.get().getProduct()).isEqualTo(product1);
    }

    @Test
    @DisplayName("findWishByIdAndMemberEmail 테스트")
    void findWishByIdAndMemberEmail() {
        Optional<Wish> foundWish = wishRepository.findWishByIdAndMemberEmail(wish1.getId(), userInfo1.getEmail());
        assertThat(foundWish).isPresent();
        assertThat(foundWish.get()).isEqualTo(wish1);
    }

    @Test
    @DisplayName("존재하지 않는 Wish 조회 테스트")
    void findNonExistentWish() {
        Optional<Wish> nonExistentWish = wishRepository.findByUserInfoIdAndProductId(999L, 999L);
        assertThat(nonExistentWish).isEmpty();
    }

    @Test
    @DisplayName("위시 아이템 수량 업데이트 테스트")
    void updateWishQuantity() {
        wish1.setCount(5);
        wishRepository.save(wish1);

        Optional<Wish> updatedWishOptional = wishRepository.findByUserInfoIdAndProductId(userInfo1.getId(), product1.getId());
        assertThat(updatedWishOptional).isPresent();
        assertThat(updatedWishOptional.get().getCount()).isEqualTo(5L);
    }

    @Test
    @DisplayName("여러 사용자의 위시 리스트 테스트")
    void multipleUserWishLists() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Wish> wishesForUser1 = wishRepository.findByUserInfoId(userInfo1.getId(), pageable);
        Page<Wish> wishesForUser2 = wishRepository.findByUserInfoId(userInfo2.getId(), pageable);

        assertThat(wishesForUser1.getContent()).hasSize(1);
        assertThat(wishesForUser1.getContent().get(0).getCount()).isEqualTo(1L);
        assertThat(wishesForUser2.getContent()).hasSize(1);
        assertThat(wishesForUser2.getContent().get(0).getCount()).isEqualTo(2L);

        assertThat(wishRepository.findAll()).hasSize(2);
    }
}
