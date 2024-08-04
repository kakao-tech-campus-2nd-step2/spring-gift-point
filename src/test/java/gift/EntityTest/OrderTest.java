package gift.EntityTest;

import gift.Model.Entity.*;
import gift.Model.Value.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OrderTest{

    private Member member;
    private Option option;
    private LocalDateTime orderDateTime;

    @BeforeEach
    void beforEach(){
        Category category = new Category("식품", "#812f3D", "식품 url", "");
        Product product = new Product("아메리카노", 4000, "아메리카노url", category);
        option = new Option("옵션1", 10, product);
        member = new Member("woo6388@naver.com", "123456789", 5000);
        orderDateTime = LocalDateTime.now();
    }

    @Test
    void creationTest() {
        Order order = new Order(option, member, 5, orderDateTime, "주문1 메세지", null);

        assertAll(
                () -> assertThat(order.getOption()).isEqualTo(option),
                () -> assertThat(order.getMember()).isEqualTo(member),
                () -> assertThat(order.getQuantity().getValue()).isEqualTo(5),
                () -> assertThat(order.getOrderDateTime()).isEqualTo(orderDateTime),
                () -> assertThat(order.getMessage().get()).isEqualTo("주문1 메세지"),
                () -> assertThat(order.getCashReceipt()).isEmpty()
        );
    }

    @Test
    void validateTest() {
        Option nullOption = null;
        Member nullMember = null;
        Quantity nullQuantity = null;
        LocalDateTime nullOrderDateTime = null;

        assertAll(
                ()-> assertThatThrownBy(() -> new Order(nullOption, member,5, orderDateTime, "주문1 메세지" , null ))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> new Order(option, nullMember, 5, orderDateTime, "주문1 메세지", null))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> new Order(option, member, nullQuantity, orderDateTime, "주문1 메세지", null))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> new Order(option, member, 5, nullOrderDateTime, "주문1 메세지", null))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    void checkBelongsToTest(){
        Order order = new Order(option, member, 5, orderDateTime, "주문1 메세지", null);
        Member member2 = new Member("qoo6388@naver.com", "123456789", 5000);

        assertThatThrownBy(()-> order.checkOrderBelongsToMember(member2))
                .isInstanceOf(IllegalArgumentException.class);

    }
}
