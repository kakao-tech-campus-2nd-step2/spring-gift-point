package gift.RepositoryTest;

import gift.Model.Entity.*;
import gift.Model.Value.Quantity;
import gift.Repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class OrderRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OptionRepository optionRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    OrderRepository orderRepository;

    private Member member;
    private Option option;
    private LocalDateTime orderDateTime;


    @BeforeEach
    void beforeEach(){
        Category category = categoryRepository.save(new Category("카테고리", "#812f3D", "카테고리 url", "카테고리 description"));
        Product product = productRepository.save(new Product("아메리카노", 4000, "아메리카노url", category));

        member = memberRepository.save(new Member("woo6388@naver.com", "123456789"));
        option = optionRepository.save(new Option("옵션1", 10, product));
        orderDateTime = LocalDateTime.now();
    }

    @Test
    void saveTest(){
        Order order = new Order(option, member, 5, orderDateTime, "주문1 메세지");
        assertThat(order.getId()).isNull();
        orderRepository.save(order);
        assertThat(order.getId()).isNotNull();
    }

    @Test
    void findAllByMemberTest(){
        orderRepository.save(new Order(option, member, 5, orderDateTime, "주문1 메세지"));
        orderRepository.save(new Order(option, member, 10, orderDateTime, "주문2 메세지"));
        List<Order> actual =  orderRepository.findAllByMember(member);

        assertAll(
                () -> assertThat(actual.get(0).getOption()).isEqualTo(option),
                () -> assertThat(actual.get(0).getQuantity().getValue()).isEqualTo(5),
                () -> assertThat(actual.get(1).getOption()).isEqualTo(option),
                () -> assertThat(actual.get(1).getQuantity().getValue()).isEqualTo(10)
        );
    }

    @Test
    void updateTest(){
        Order order= orderRepository.save(new Order(option, member, 5, orderDateTime, "주문1 메세지"));
        order.addQuantity(new Quantity(3));
        Optional<Order> actualOptionalOrder1 = orderRepository.findById(order.getId());
        Order actualOrder1 = actualOptionalOrder1.get();

        assertThat(actualOrder1.getQuantity().getValue()).isEqualTo(8);

        order.subtractQuantity(new Quantity(3));
        Optional<Order> actualOptionalOrder2 = orderRepository.findById(order.getId());
        Order actualOrder2 = actualOptionalOrder2.get();

        assertThat(actualOrder2.getQuantity().getValue()).isEqualTo(5);
    }

    @Test
    void deleteById(){
        Order order = orderRepository.save(new Order(option, member, 5, orderDateTime, "주문1 메세지"));
        orderRepository.deleteById(order.getId());
        Optional<Order> actual = orderRepository.findById(order.getId());
        assertThat(actual).isEmpty();
    }
}
