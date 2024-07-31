package gift.doamin.wishlist.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.doamin.category.entity.Category;
import gift.doamin.category.repository.JpaCategoryRepository;
import gift.doamin.product.entity.Product;
import gift.doamin.product.repository.JpaProductRepository;
import gift.doamin.user.entity.User;
import gift.doamin.user.entity.UserRole;
import gift.doamin.user.repository.JpaUserRepository;
import gift.doamin.wishlist.entity.Wish;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
    JpaCategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        User user1 = jpaUserRepository.save(
            new User("test1@test.com", "test", "test1", UserRole.SELLER));
        Category category = categoryRepository.findById(1L).get();
        jpaProductRepository.save(new Product(user1, category, "test", 1, "test.png"));
        jpaProductRepository.save(new Product(user1, category, "test2", 1, "test2.png"));
    }

    @Test
    void save() {
        User user = jpaUserRepository.findByEmail("test1@test.com").get();
        Product product = jpaProductRepository.findByName("test").getFirst();
        Wish wish = new Wish(user, product, 3);

        Wish savedWish = jpaWishListRepository.save(wish);

        assertThat(savedWish.getId()).isNotNull();
    }

    @Test
    void existsByUserIdAndProductId() {
        User user = jpaUserRepository.findByEmail("test1@test.com").get();
        Product product = jpaProductRepository.findByName("test").getFirst();
        Wish wish = new Wish(user, product, 3);
        jpaWishListRepository.save(wish);

        assertAll(
            () -> assertThat(jpaWishListRepository.existsByUserIdAndProductId(user.getId(),
                product.getId())).isTrue(),
            () -> assertThat(
                jpaWishListRepository.existsByUserIdAndProductId(user.getId(), 0L)).isFalse()
        );
    }

    @Test
    void findAllByUserId() {
        User user = jpaUserRepository.findByEmail("test1@test.com").get();
        Product product = jpaProductRepository.findByName("test").getFirst();
        Wish wish1 = new Wish(user, product, 1);
        jpaWishListRepository.save(wish1);
        Product product2 = jpaProductRepository.findByName("test2").getFirst();
        Wish wish2 = new Wish(user, product2, 2);
        jpaWishListRepository.save(wish2);
        Pageable pageable = PageRequest.of(0, 5);

        List<Wish> wishes = jpaWishListRepository.findAllByUserId(user.getId(), pageable)
            .getContent();

        assertThat(wishes.size()).isEqualTo(2);
    }

    @Test
    void findByUserIdAndProductId() {
        User user = jpaUserRepository.findByEmail("test1@test.com").get();
        Product product = jpaProductRepository.findByName("test").getFirst();
        Wish wish = new Wish(user, product, 3);
        Wish savedWish = jpaWishListRepository.save(wish);

        var foundWishList = jpaWishListRepository.findByUserIdAndProductId(user.getId(),
            product.getId());

        assertThat(foundWishList.get()).isEqualTo(savedWish);
    }

    @Test
    void deleteById() {
        User user = jpaUserRepository.findByEmail("test1@test.com").get();
        Product product = jpaProductRepository.findByName("test").getFirst();
        Wish wish = new Wish(user, product, 3);
        Wish savedWish = jpaWishListRepository.save(wish);

        jpaWishListRepository.deleteById(savedWish.getId());
        Optional<Wish> foundWishList = jpaWishListRepository.findById(savedWish.getId());

        assertThat(foundWishList.isEmpty()).isTrue();

    }

}