package gift.doamin.wishlist.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.doamin.category.entity.Category;
import gift.doamin.category.repository.JpaCategoryRepository;
import gift.doamin.product.entity.Option;
import gift.doamin.product.entity.Product;
import gift.doamin.product.repository.JpaProductRepository;
import gift.doamin.product.repository.OptionRepository;
import gift.doamin.user.entity.User;
import gift.doamin.user.entity.UserRole;
import gift.doamin.user.repository.JpaUserRepository;
import gift.doamin.wishlist.dto.WishResponse;
import gift.doamin.wishlist.entity.Wish;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
class JpaWishListRepositoryTest {

    @Autowired
    JpaWishListRepository jpaWishListRepository;

    @Autowired
    JpaUserRepository jpaUserRepository;

    @Autowired
    JpaProductRepository jpaProductRepository;

    @Autowired
    OptionRepository optionRepository;

    @Autowired
    JpaCategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        User user1 = jpaUserRepository.save(
            new User("test1@test.com", "test", "test1", UserRole.SELLER));
        Category category = categoryRepository.save(new Category("test", "#000000", "", "테스트 카테고리"));
        Product product1 = jpaProductRepository.save(new Product(user1, category, "test", 1, "test.png"));
        optionRepository.save(new Option(product1, "옵션1", 11));
        Product product2 = jpaProductRepository.save(new Product(user1, category, "test2", 1, "test2.png"));
        optionRepository.save(new Option(product2, "옵션2", 22));
    }

    @Test
    void save() {
        User user = jpaUserRepository.findByEmail("test1@test.com").get();
        Option option = optionRepository.findByName("옵션1").get();
        Wish wish = new Wish(user, option, 3);

        Wish savedWish = jpaWishListRepository.save(wish);

        assertThat(savedWish.getId()).isNotNull();
    }

    @Test
    void existsByUserIdAndOptionId() {
        User user = jpaUserRepository.findByEmail("test1@test.com").get();
        Option option = optionRepository.findByName("옵션1").get();
        Wish wish = new Wish(user, option, 3);
        jpaWishListRepository.save(wish);

        assertAll(
            () -> assertThat(jpaWishListRepository.existsByUserIdAndOptionId(user.getId(),
                option.getId())).isTrue(),
            () -> assertThat(
                jpaWishListRepository.existsByUserIdAndOptionId(user.getId(), 0L)).isFalse()
        );
    }

    @Test
    void findAllByUserId() {
        User user = jpaUserRepository.save(
            new User("test2@test.com", "test", "test1", UserRole.SELLER));
        Option option = optionRepository.findByName("옵션1").get();
        Wish wish1 = new Wish(user, option, 1);
        jpaWishListRepository.save(wish1);
        Option option2 = optionRepository.findByName("옵션2").get();
        Wish wish2 = new Wish(user, option2, 2);
        jpaWishListRepository.save(wish2);
        Pageable pageable = PageRequest.of(0, 5);

        var wishList = jpaWishListRepository.findAllByUserId(user.getId(), pageable).getContent();
        assertThat(wishList.size()).isEqualTo(2);
    }

    @Test
    void findByUserIdAndOptionId() {
        User user = jpaUserRepository.findByEmail("test1@test.com").get();
        Option option = optionRepository.findByName("옵션1").get();
        Wish wish = new Wish(user, option, 3);
        Wish savedWish = jpaWishListRepository.save(wish);

        var foundWishList = jpaWishListRepository.findByUserIdAndOptionId(user.getId(),
            option.getId());

        assertThat(foundWishList.get()).isEqualTo(savedWish);
    }

    @Test
    void deleteById() {
        User user = jpaUserRepository.findByEmail("test1@test.com").get();
        Option option = optionRepository.findByName("옵션1").get();
        Wish wish = new Wish(user, option, 3);
        Wish savedWish = jpaWishListRepository.save(wish);

        jpaWishListRepository.deleteById(savedWish.getId());
        Optional<Wish> foundWishList = jpaWishListRepository.findById(savedWish.getId());

        assertThat(foundWishList.isEmpty()).isTrue();

    }

}