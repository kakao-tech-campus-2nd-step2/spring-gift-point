package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.administrator.category.Category;
import gift.administrator.category.CategoryRepository;
import gift.administrator.option.Option;
import gift.administrator.product.Product;
import gift.administrator.product.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    private Category category;
    private Product product;
    private List<Option> options;

    @BeforeEach
    void beforeEach() {
        category = new Category("상품권", null, null, null);
        categoryRepository.save(category);
        Option option = new Option("L", 3, null);
        options = new ArrayList<>(List.of(option));
        product = new Product("라이언", 1000, "image.jpg", category, options);
        option.setProduct(product);
    }

    @Test
    @DisplayName("상품 추가 테스트")
    void save() {
        //Given
        Product expected = new Product("라이언", 1000, "image.jpg", category, options);

        //When
        Product actual = productRepository.save(product);

        //Then
        assertThat(actual)
            .extracting(Product::getName, Product::getPrice, Product::getImageUrl,
                Product::getCategory, p -> p.getOptions().getFirst().getName(),
                p -> p.getOptions().getFirst().getQuantity(),
                p -> p.getOptions().getFirst().getProduct())
            .containsExactly(expected.getName(), expected.getPrice(), expected.getImageUrl(),
                expected.getCategory(), expected.getOptions().getFirst().getName(),
                expected.getOptions().getFirst().getQuantity(),
                expected.getOptions().getFirst().getProduct());
    }

    @Test
    @DisplayName("상품 아이디로 찾기 테스트")
    void findById() {
        //Given
        productRepository.save(product);
        Product expected = new Product("라이언", 1000, "image.jpg", category, options);

        //When
        Optional<Product> actual = productRepository.findById(product.getId());

        //Then
        assertThat(actual).isPresent();
        assertThat(actual.get())
            .extracting(Product::getName, Product::getPrice, Product::getImageUrl,
                Product::getCategory, p -> p.getOptions().getFirst().getName(),
                p -> p.getOptions().getFirst().getQuantity(),
                p -> p.getOptions().getFirst().getProduct())
            .containsExactly(expected.getName(), expected.getPrice(), expected.getImageUrl(),
                expected.getCategory(), expected.getOptions().getFirst().getName(),
                expected.getOptions().getFirst().getQuantity(),
                expected.getOptions().getFirst().getProduct());
    }

    @Test
    @DisplayName("전체 상품 찾기 테스트")
    void findAll() {
        //Given
        int page = 0;
        int size = 10;
        String sortBy = "id";
        Sort.Direction direction = Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        productRepository.save(product);
        Category category = new Category(1L, "도서", null, null, null);
        Option option = new Option("어린왕자", 3, null);
        category = categoryRepository.save(category);
        Product product1 = new Product(1L, "Product1", 1000, "imageUrl1", category, List.of(option));
        product1 = productRepository.save(product1);
        option.setProduct(product1);

        List<Product> expected = List.of(product, product1);

        //When
        Page<Product> result = productRepository.findAll(pageable);

        //Then
        assertThat(result.getTotalElements()).isEqualTo(expected.size());
        assertThat(result.getContent().get(0))
            .extracting(Product::getName, Product::getPrice, Product::getImageUrl, Product::getCategory)
            .containsExactly(expected.getFirst().getName(), expected.getFirst().getPrice(),
                expected.getFirst().getImageUrl(), expected.getFirst().getCategory());
        assertThat(result.getContent().get(1))
            .extracting(Product::getName, Product::getPrice, Product::getImageUrl, Product::getCategory)
            .containsExactly(expected.get(1).getName(), expected.get(1).getPrice(),
                expected.get(1).getImageUrl(), expected.get(1).getCategory());
    }

    @Test
    @DisplayName("상품 아이디로 존재 여부 확인 시 존재함")
    void existsById() {
        //Given
        productRepository.save(product);

        //When
        boolean actual = productRepository.existsById(product.getId());

        //Then
        assertThat(actual).isEqualTo(true);
    }

    @Test
    @DisplayName("상품 아이디로 존재 여부 확인 시 존재하지 않음")
    void notExistsById() {
        //Given
        productRepository.save(product);

        //When
        boolean actual = productRepository.existsById(product.getId() + 1);

        //Then
        assertThat(actual).isEqualTo(false);
    }

    @Test
    @DisplayName("상품 이름으로 상품 존재 여부 확인 시 존재함")
    void existsByName() {
        //Given
        productRepository.save(product);

        //When
        boolean actual = productRepository.existsByName(product.getName());

        //Then
        assertThat(actual).isEqualTo(true);
    }

    @Test
    @DisplayName("상품 이름으로 상품 존재 여부 확인 시 존재하지 않음")
    void notExistsByName() {
        //Given
        productRepository.save(product);

        //When
        boolean actual = productRepository.existsByName("라면");

        //Then
        assertThat(actual).isEqualTo(false);
    }

    @Test
    @DisplayName("상품 이름 존재하지만 상품 아이디로 존재하지 않음")
    void existsByNameAndIdNot() {
        //Given
        productRepository.save(product);

        //When
        boolean actual = productRepository.existsByNameAndIdNot(product.getName(), product.getId() + 1);

        //Then
        assertThat(actual).isEqualTo(true);
    }

    @Test
    @DisplayName("상품 이름 존재하지 않거나 상품 아이디로 존재함")
    void existsByNameAndIdNotReturnsFalse() {
        //Given
        productRepository.save(product);

        //When
        boolean actual = productRepository.existsByNameAndIdNot(product.getName(), product.getId());

        //Then
        assertThat(actual).isEqualTo(false);
    }

    @Test
    @DisplayName("상품 삭제 테스트")
    void delete() {
        //Given
        productRepository.save(product);

        //When
        productRepository.delete(product);
        Optional<Product> actual = productRepository.findById(product.getId());

        //Then
        assertThat(actual).isNotPresent();
    }

    @Test
    @DisplayName("상품아이디로 상품 삭제 테스트")
    void deleteById() {
        //Given
        productRepository.save(product);

        //When
        productRepository.deleteById(product.getId());
        Optional<Product> actual = productRepository.findById(product.getId());

        //Then
        assertThat(actual).isNotPresent();
    }
}
