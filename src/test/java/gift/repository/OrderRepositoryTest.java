package gift.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.model.Category;
import gift.model.Member;
import gift.model.Option;
import gift.model.Order;
import gift.model.Product;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OptionRepository optionRepository;

    private Order savedOrder;
    private Member savedMember;
    private Product savedProduct;
    private Option savedOption;
    private Category savedCategory;

    @BeforeEach
    public void setUp() {
        Category category = new Category(null, "사치품", "#007700", "임시 이미지", "임시 설명");
        savedCategory = categoryRepository.save(category);
        Member member = new Member(null, "email@email.com", "password", "user", 0L);
        savedMember = memberRepository.save(member);
        Product product = new Product(null, "상품", "100", savedCategory, "https://kakao");
        savedProduct = productRepository.save(product);
        Option option = new Option(null, "옵션", 1L, savedProduct);
        savedOption = optionRepository.save(option);
        Order order = new Order(null, savedOption, 1L, LocalDateTime.now(), "메시지", savedMember);
        savedOrder = orderRepository.save(order);
    }

    @Test
    void testSaveOrder() {
        assertAll(
            () -> assertThat(savedOrder.getId()).isNotNull(),
            () -> assertThat(savedOrder.getOption().getId()).isEqualTo(savedOption.getId()),
            () -> assertThat(savedOrder.getMember().getEmail()).isEqualTo(savedMember.getEmail()),
            () -> assertThat(savedOrder.getQuantity()).isEqualTo(1L),
            () -> assertThat(savedOrder.getMessage()).isEqualTo("메시지"),
            () -> assertThat(savedOrder.getOrderDateTime()).isNotNull()
        );
    }

}