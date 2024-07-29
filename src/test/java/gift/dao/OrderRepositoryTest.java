package gift.dao;

import gift.member.dao.MemberRepository;
import gift.member.entity.Member;
import gift.order.dao.OrderRepository;
import gift.order.entity.Order;
import gift.product.dao.OptionRepository;
import gift.product.dao.ProductRepository;
import gift.product.entity.Category;
import gift.product.entity.Option;
import gift.product.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import testFixtures.CategoryFixture;
import testFixtures.MemberFixture;
import testFixtures.OptionFixture;
import testFixtures.ProductFixture;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("주문 추가 및 조회 테스트")
    void saveAndFindById() {
        Category category = CategoryFixture.createCategory("category");
        Product product = ProductFixture.createProduct("product", category);
        Product savedProduct = productRepository.save(product);

        Option option = OptionFixture.createOption("옵션", savedProduct);
        Option savedOption = optionRepository.save(option);

        Member member = MemberFixture.createMember("test@email.com");
        Member savedMember = memberRepository.save(member);

        Order order = new Order("message", savedOption, savedMember);

        Order savedOrder = orderRepository.save(order);
        Order foundOrder = orderRepository.findById(savedOrder.getId())
                .orElse(null);

        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getId()).isEqualTo(savedOrder.getId());
        assertThat(foundOrder.getMessage()).isEqualTo(savedOrder.getMessage());
    }

}