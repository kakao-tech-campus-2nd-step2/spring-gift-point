package gift.main.repository;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final WishProductRepository wishProductRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Autowired
    public ProductRepositoryTest(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            WishProductRepository wishProductRepository,
            UserRepository userRepository,
            EntityManager entityManager) {

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.wishProductRepository = wishProductRepository;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
    }


}