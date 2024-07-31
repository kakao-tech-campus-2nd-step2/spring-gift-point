package gift.wishes;

import gift.core.domain.product.Product;
import gift.core.domain.product.ProductCategory;
import gift.core.domain.user.User;
import gift.core.domain.user.UserAccount;
import gift.product.infrastructure.persistence.repository.JpaProductCategoryRepository;
import gift.product.infrastructure.persistence.repository.JpaProductRepository;
import gift.product.infrastructure.persistence.entity.ProductCategoryEntity;
import gift.product.infrastructure.persistence.entity.ProductEntity;
import gift.user.infrastructure.persistence.repository.JpaUserRepository;
import gift.user.infrastructure.persistence.entity.UserEntity;
import gift.wishes.infrastructure.persistence.JpaWishRepository;
import gift.wishes.infrastructure.persistence.WishEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WishRepositoryTests {
    @Autowired
    private JpaWishRepository jpaWishRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private JpaProductRepository jpaProductRepository;

    @Autowired
    private JpaProductCategoryRepository jpaProductCategoryRepository;

    private ProductEntity product;
    private ProductCategoryEntity category;
    private UserEntity user;

    @BeforeEach
    public void setUp() {
        user = jpaUserRepository.save(
                UserEntity.fromDomain(new User("test", new UserAccount("test", "test")))
        );
        category = jpaProductCategoryRepository.save(
                ProductCategoryEntity.fromDomain(sampleCategory())
        );
        product = jpaProductRepository.save(ProductEntity.fromDomain(
                new Product(0L, "test", 100, "test", category.toDomain()))
        );
    }

    @Test
    public void saveWish() {
        WishEntity wish = WishEntity.of(user, product, 1L);

        wish = jpaWishRepository.save(wish);

        assertThat(jpaWishRepository.existsByUser_IdAndProduct_Id(user.getId(), product.getId())).isTrue();
        assertThat(jpaWishRepository.findById(wish.getId()).get()).isEqualTo(wish);
    }

    @Test
    public void removeWish() {
        WishEntity wish = WishEntity.of(user, product, 1L);

        wish = jpaWishRepository.save(wish);

        jpaWishRepository.deleteByUser_IdAndProduct_Id(user.getId(), wish.getId());
        assertThat(jpaWishRepository.existsByUser_IdAndProduct_Id(wish.getId(), wish.getId())).isFalse();
    }

    @Test
    public void existsByUserIdAndProductId() {
        WishEntity wish = WishEntity.of(user, product, 1L);

        wish = jpaWishRepository.save(wish);

        assertThat(jpaWishRepository.existsByUser_IdAndProduct_Id(wish.getId(), wish.getId())).isTrue();
    }

    @Test
    public void findAllByUserId() {
        WishEntity wish = WishEntity.of(user, product, 1L);

        jpaWishRepository.save(wish);

        assertThat(jpaWishRepository.findAllByUser(user).size()).isEqualTo(1);
    }

    private static ProductCategory sampleCategory() {
        return ProductCategory.of("test", "#6c95d1", "test.jpg", "test");
    }
}
