package gift.domain;

import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import gift.dto.request.ProductUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @DisplayName("모든 상품 정보를 조회한다.")
    @Test
    void findAll() throws Exception {
        //when
        List<Product> products = productRepository.findAll();

        //then
        assertThat(products.size()).isEqualTo(0);
    }

    @DisplayName("상품 하나를 ID로 조회한다.")
    @Test
    void findById() throws Exception {
        //given
        Category category = categoryRepository.save(new Category("교환권"));
        Product product = new Product("아이스 아메리카노", 3500, "https://examle.com", category);
        productRepository.save(product);

        List<Product> products = productRepository.findAll();
        Long productId = products.get(0).getId();

        //when
        Product foundProduct = productRepository.findById(productId).get();

        //then
        assertThat(productId).isEqualTo(foundProduct.getId());
    }

    @DisplayName("상품 하나를 추가한다.")
    @Test
    void save() throws Exception {
        //given & when
        Category category = categoryRepository.save(new Category("교환권"));
        Product product = new Product("아이스 아메리카노", 3500, "https://examle.com", category);
        productRepository.save(product);

        //then
        assertThat(productRepository.findAll().size()).isEqualTo(1);
    }

    @DisplayName("상품 정보를 수정한다.")
    @Test
    void edit() throws Exception {
        //given
        Category category = categoryRepository.save(new Category("교환권"));
        Product product = new Product("아이스 아메리카노", 3500, "https://examle.com", category);
        Product savedProduct = productRepository.save(product);
        Long productId = savedProduct.getId();

        ProductUpdateRequest request = new ProductUpdateRequest("망고 스무디", 5000, "https://test.com", category.getId());

        //when
        savedProduct.changeName(request.getName());
        savedProduct.changePrice(request.getPrice());
        savedProduct.changeImageUrl(request.getImageUrl());

        Product foundProduct = productRepository.findById(productId).get();

        //then
        assertThat(foundProduct.getName()).isEqualTo(request.getName());
        assertThat(foundProduct.getPrice()).isEqualTo(request.getPrice());
        assertThat(foundProduct.getImageUrl()).isEqualTo(request.getImageUrl());
    }

    @DisplayName("상품 아이디를 받아, 해당하는 상품을 삭제한다.")
    @Test
    void deleteById() throws Exception {
        //given
        Category category = categoryRepository.save(new Category("교환권"));
        Product product = new Product("아이스 아메리카노", 3500, "https://examle.com", category);
        productRepository.save(product);

        List<Product> products = productRepository.findAll();
        Long productId = products.get(0).getId();

        int prevSize = productRepository.findAll().size();

        //when
        productRepository.deleteById(productId);

        //then
        int currSize = productRepository.findAll().size();

        assertThat(currSize).isEqualTo(prevSize - 1);
    }

}
