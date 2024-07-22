package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.model.Category;
import gift.model.Member;
import gift.model.Option;
import gift.model.Product;
import gift.model.Wish;
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
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
public class WishRepositoryTest {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OptionRepository optionRepository;

    @BeforeEach
    public void setUp() {
        wishRepository.deleteAll();
    }

    @Test
    @DisplayName("위시리스트 항목 추가 및 ID로 조회")
    public void testSaveAndFindById() {
        Category category = new Category("Test Category", "#000000", "imageUrl", "description");
        categoryRepository.save(category);

        Product product = new Product("Product1", 100, "imageUrl1", category);
        Product savedProduct = productRepository.save(product);

        Option option = new Option("Option1", 100, savedProduct);
        optionRepository.save(option);

        Member member = new Member(1L, "test@example.com", "password");

        Wish wish = new Wish(member, savedProduct);
        Wish savedWish = wishRepository.save(wish);
        Optional<Wish> foundWish = wishRepository.findById(savedWish.getId());

        assertThat(foundWish).isPresent();
        assertThat(foundWish.get().getMemberId()).isEqualTo(1L);
        assertThat(foundWish.get().getProductId()).isEqualTo(savedProduct.getId());
    }

    @Test
    @DisplayName("모든 위시리스트 항목 조회 (페이지네이션 적용)")
    public void testFindAllByMemberIdWithPagination() {
        Category category = new Category("Test Category", "#000000", "imageUrl", "description");
        categoryRepository.save(category);

        Product product1 = new Product("Product1", 100, "imageUrl1", category);
        Product product2 = new Product("Product2", 200, "imageUrl2", category);

        productRepository.save(product1);
        productRepository.save(product2);

        Option option1 = new Option("Option1", 100, product1);
        Option option2 = new Option("Option2", 200, product1);
        Option option3 = new Option("Option3", 300, product2);
        optionRepository.saveAll(List.of(option1, option2, option3));

        Member member = new Member(1L, "test@example.com", "password");

        Wish wish1 = new Wish(member, product1);
        Wish wish2 = new Wish(member, product2);

        wishRepository.save(wish1);
        wishRepository.save(wish2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Wish> wishPage = wishRepository.findAllByMember_Id(1L, pageable);

        assertThat(wishPage.getTotalElements()).isEqualTo(2);
        assertThat(wishPage.getContent()).hasSize(
            (int) Math.min(2, pageable.getPageSize()));
    }

    @Test
    @DisplayName("위시리스트 항목 삭제")
    public void testDeleteById() {
        Category category = new Category("Test Category", "#000000", "imageUrl", "description");
        categoryRepository.save(category);

        Product product = new Product("Product1", 100, "imageUrl1", category);
        Product savedProduct = productRepository.save(product);

        Option option = new Option("Option1", 100, savedProduct);
        optionRepository.save(option);

        Member member = new Member(1L, "test@example.com", "password");

        Wish wish = new Wish(member, savedProduct);
        Wish savedWish = wishRepository.save(wish);

        wishRepository.deleteById(savedWish.getId());
        Optional<Wish> foundWish = wishRepository.findById(savedWish.getId());

        assertThat(foundWish).isNotPresent();
    }

    @Test
    @DisplayName("회원 ID와 상품 ID로 위시리스트 항목 존재 여부 확인")
    public void testExistsByMemberIdAndProductId() {
        Category category = new Category("Test Category", "#000000", "imageUrl", "description");
        categoryRepository.save(category);

        Product product = new Product("Product1", 100, "imageUrl1", category);
        Product savedProduct = productRepository.save(product);

        Option option = new Option("Option1", 100, savedProduct);
        optionRepository.save(option);

        Member member = new Member(1L, "test@example.com", "password");

        Wish wish = new Wish(member, savedProduct);
        wishRepository.save(wish);

        boolean exists = wishRepository.existsByMember_IdAndProduct_Id(1L, savedProduct.getId());
        assertThat(exists).isTrue();
    }
}
