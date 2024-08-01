package gift.Product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.domain.member.Member;
import gift.domain.category.Category;
import gift.domain.category.JpaCategoryRepository;
import gift.domain.wish.JpaWishRepository;
import gift.domain.product.JpaProductRepository;
import gift.domain.product.Product;
import gift.domain.member.JpaMemberRepository;
import gift.domain.wish.Wish;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@DataJpaTest
@ActiveProfiles("test")
public class ProductRepositoryTest {

    @Autowired
    private JpaProductRepository productRepository;
    @Autowired
    private JpaCategoryRepository categoryRepository;
    @Autowired
    private JpaMemberRepository memberRepository;
    @Autowired
    private JpaWishRepository cartItemRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Product product1;
    private Product product2;
    private Category ethiopia;
    private Category jamaica;

    public ProductRepositoryTest() {
    }

    @BeforeEach
    void setUp() {
        ethiopia = categoryRepository.saveAndFlush(new Category("에티오피아산", "에티오피아 산 원두를 사용했습니다.","color code", "http://www.example.com/index.html"));
        jamaica = categoryRepository.saveAndFlush(new Category("자메이카산", "자메이카산 원두를 사용했습니다.","color code", "http://www.example.com/index.html"));

        Product product1 = new Product("아이스 아메리카노 T", ethiopia, 4500,"description",
            "https://example.com/image.jpg");
        this.product1 = product1;
        Product product2 = new Product("아이스 말차라떼 T", jamaica, 4500,"description",
            "https://example.com/image.jpg");
        this.product2 = product2;
    }

    @Test
    @Description("상품 정상 저장")
    void save() {
        // when
        Product savedProduct = productRepository.saveAndFlush(product1);
        clear();
        Product findProduct = productRepository.findById(savedProduct.getId()).get();

        //then
        assertThat(savedProduct).isEqualTo(findProduct);
    }

    @Test
    @Description("상품 저장 시 이름 중복 검증")
    void saveWithSameName() {
        // given
        Product product1 = new Product("아이스 아메리카노 T", ethiopia, 4500,"description",
            "https://example.com/image.jpg");
        Product product2 = new Product("아이스 아메리카노 T", jamaica, 4700,"description",
            "https://example.com/image.jpg");

        // when
        productRepository.save(product1);

        /// then
        assertThrows(DataIntegrityViolationException.class,
            () -> productRepository.saveAndFlush(product2));
    }


    @Test
    @Description("상품 수정")
    void update() {
        // given
        productRepository.saveAndFlush(product1);
        clear();
        Product findProduct = productRepository.findById(product1.getId()).get();

        // when
        findProduct.setPrice(4700);
        findProduct.setImageUrl("https://example.com/imageModified.jpg");

        Product savedProduct = productRepository.saveAndFlush(findProduct);

        // then
        assertThat(findProduct).isEqualTo(savedProduct);
    }

    @Test
    @Description("상품 삭제")
    void delete() {
        // when
        Product savedProduct = productRepository.saveAndFlush(product1);
        productRepository.deleteById(savedProduct.getId());
        flushAndClear();

        // then
        Optional<Product> findProduct = productRepository.findById(savedProduct.getId());
        assertThat(findProduct.isEmpty()).isEqualTo(true);
    }

    @Test
    @Description("상품을 참조하는 장바구니 정보 존재하면 장바구니도 함께 삭제함")
    void deleteConstraintViolationError() {
        // given
        Product savedProduct = productRepository.saveAndFlush(product1);
        Member savedMember = memberRepository.saveAndFlush(new Member("minji@example.com", "password1"));
        Wish savedWish = cartItemRepository.saveAndFlush(new Wish(savedMember, savedProduct));
        clear();

        // when
        productRepository.deleteById(savedProduct.getId());
        flush();

        // then
        assertThat(cartItemRepository.existsById(savedWish.getId())).isFalse();
    }

    @Test
    @Description("상품들 삭제")
    void deleteAllByIdsIn() {
        // given
        Product savedProduct1 = productRepository.saveAndFlush(product1);
        Product savedProduct2 = productRepository.saveAndFlush(product2);
        List<Long> ids = new ArrayList<>();
        ids.add(savedProduct1.getId());
        ids.add(savedProduct2.getId());

        // when
        productRepository.deleteAllByIdIn(ids);
        List<Product> products = productRepository.findAll();
        // then
        assertThat(products.size()).isEqualTo(0);
    }

    @Test
    @Description("정상 페이징 확인")
    void testPagingSuccess() {
        // given
        productRepository.saveAndFlush(product1);
        productRepository.saveAndFlush(product2);
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Direction.ASC, "price"));
        clear();

        // when
        Page<Product> products = productRepository.findAll(pageRequest);
        System.out.println("products = " + products);

        // then
        assertAll(
            () -> assertThat(products.getTotalElements()).isEqualTo(2), // 전체 Product 개수
            () -> assertThat(products.getTotalPages()).isEqualTo(1), // 전체 페이지 개수
            () -> assertThat(products.getNumber()).isEqualTo(pageRequest.getPageNumber()),
            // 현재 페이지 번호
            () -> assertThat(products.getSize()).isEqualTo(pageRequest.getPageSize())
            // 페이지당 보여지는 Product 개수
        );
    }

    private void flush() {
        entityManager.flush();
    }

    private void clear() {
        entityManager.clear();
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
