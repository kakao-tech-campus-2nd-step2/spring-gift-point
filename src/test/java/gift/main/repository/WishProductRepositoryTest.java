package gift.main.repository;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WishProductRepositoryTest {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final EntityManager entityManager;
    private final WishProductRepository wishProductRepository;

    @Autowired
    WishProductRepositoryTest(UserRepository userRepository,
                              ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              EntityManager entityManager,
                              WishProductRepository wishProductRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.entityManager = entityManager;
        this.wishProductRepository = wishProductRepository;
    }

}