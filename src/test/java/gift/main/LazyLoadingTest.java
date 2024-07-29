package gift.main;

import jakarta.persistence.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

interface ProductTestRepository extends JpaRepository<ProductTest, Long> {
}

interface OptionTestRepository extends JpaRepository<OptionTest, Long> {
}

@Entity
class ProductTest {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<OptionTest> options = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public List<OptionTest> getOptions() {
        return options;
    }

    public void addOption(OptionTest option) {
        options.add(option);
    }
}

@Entity
class OptionTest {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @ManyToOne
    private ProductTest product;

    public void setProduct(ProductTest product) {
        this.product = product;
    }
}

@Transactional
@SpringBootTest
class LazyLoadingTest {
    @Autowired
    private ProductTestRepository productRepository;

    @Autowired
    private OptionTestRepository optionRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void test() {
        var savedProduct = productRepository.save(new ProductTest());
        var option = new OptionTest();
        option.setProduct(savedProduct);
        optionRepository.save(option);

        var product = productRepository.findById(savedProduct.getId()).orElseThrow();

        // 테스트 실패
//        entityManager.clear();
        assertThat(productRepository.findById(product.getId()).get().getOptions()).hasSize(1);
    }

}

