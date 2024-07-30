package gift.main.service;

import gift.main.dto.*;
import gift.main.entity.Category;
import gift.main.entity.Product;
import gift.main.entity.User;
import gift.main.entity.WishProduct;
import gift.main.repository.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductServiceTest {

    private final ProductService productService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final WishProductRepository wishProductRepository;
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final EntityManager entityManager;

    @Autowired
    public ProductServiceTest(
            ProductService productService,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            WishProductRepository wishProductRepository,
            ProductRepository productRepository,
            OptionRepository optionRepository,
            EntityManager entityManager) {

        this.productService = productService;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.wishProductRepository = wishProductRepository;
        this.productRepository = productRepository;
        this.optionRepository = optionRepository;
        this.entityManager = entityManager;
    }

    /*
     * <테스트 수행 내용>
     * ProductService 의 registerProduct 테스트
     * 1. 프로덕트 정상 저장
     * 2. 옵션리스트 정상 저장
     */
    @Test
    @Transactional
    void saveProductTest() {
        //given
        User seller = new User("seller", "seller@", "1234", "ADMIN");
        User saveSeller = userRepository.save(seller);
        UserVo userVo = new UserVo(saveSeller.getId(), saveSeller.getName(), saveSeller.getEmail(), saveSeller.getRole());

        ProductRequest productRequest = new ProductRequest("testProduct1", 12000, "Url", 1);

        List<OptionRequest> options = new ArrayList<>();
        options.add(new OptionRequest("1번", 1));
        options.add(new OptionRequest("2번", 2));
        options.add(new OptionRequest("3번", 3));
        OptionListRequest optionListRequest = new OptionListRequest(options);
        ProductAllRequest productAllRequest = new ProductAllRequest(productRequest, optionListRequest);
        //when
        //ProductAllRequest productAllRequest
        productService.registerProduct(productAllRequest, userVo);

        //then
        assertThat(productRepository.existsById(1L)).isTrue();
        Product saveProduct = productRepository.findById(1L).get();

        assertThat(optionRepository.findAllByProductId(saveProduct.getId()).get().size()).isEqualTo(3);

    }

    /*
     * <테스트 수행 내용>
     * ProductService 의 deleteProduct 테스트
     * 1. 상품 정상 삭제 확인!
     * 2. 삭제시 관련 위시 상품 정상 삭제 확인!
     */
    @Test
    @Transactional
    void deleteProductWithWishProductTest() {
        //given
        User user = new User("user", "user@", "1234", "ADMIN");
        User savedUser = userRepository.save(user);

        Category category = categoryRepository.findByName("패션").get();

        Product product = new Product("testProduct1", 12000, "Url", savedUser, category);
        Product saveProduct = productRepository.save(product);

        WishProduct wishProduct = new WishProduct(saveProduct, savedUser);
        wishProductRepository.save(wishProduct);

        //when
        productService.deleteProduct(saveProduct.getId());

        //then
        assertThat(wishProductRepository.findAllByProductId(saveProduct.getId()).size()).isEqualTo(0);
        assertThat(productRepository.existsById(saveProduct.getId())).isFalse();
    }

    /*
     * <테스트 수행 내용>
     * ProductService 의 deleteProduct 테스트
     * 1. 위시가 없는 상태 에러 발생 하지 않고 상품 정상 삭제 가능!
     */
    @Test
    @Transactional
    void deleteProductTest() {
        //given
        User user = new User("user", "user@", "1234", "ADMIN");
        User savedUser = userRepository.save(user);

        Category category = categoryRepository.findByName("패션").get();

        Product product = new Product("testProduct1", 12000, "Url", savedUser, category);
        Product saveProduct = productRepository.save(product);

        //when
        productService.deleteProduct(saveProduct.getId());

        //then
        assertThat(wishProductRepository.findAllByProductId(saveProduct.getId()).size()).isEqualTo(0);
        assertThat(productRepository.existsById(saveProduct.getId())).isFalse();

    }
}