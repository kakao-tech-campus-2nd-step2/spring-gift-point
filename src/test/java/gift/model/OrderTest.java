package gift.model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.exception.InvalidInputValueException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderTest {

    private Option option;
    private Member member;
    private Category category;
    private Product product;
    private Order order;

    @BeforeEach
    void setUp() {
        member = new Member(1L, "email@email.com", "password", "user");
        category = new Category(1L, "교환권", "#007700", "임시 이미지", "임시 설명");
        product = new Product(1L, "상품", "100", category, "https://kakao");
        option = new Option(1L, "임시 옵션", 10L, product);
        order = new Order(1L, option, 1L, LocalDateTime.now(), "임시 메시지", member);
    }

    @Test
    void testCreateValidOrder() {
        assertAll(
            () -> assertThat(order.getId()).isNotNull(),
            () -> assertThat(order.getOption().getId()).isEqualTo(1L),
            () -> assertThat(order.getQuantity()).isEqualTo(1L),
            () -> assertThat(order.getMessage()).isEqualTo("임시 메시지"),
            () -> assertThat(order.getMember().getId()).isEqualTo(1L)
        );
    }

    @Test
    void testCreateWithNullQuantity() {
        try {
            Order nullQuantityOrder = new Order(1L, option, null, LocalDateTime.now(), "임시 메시지",
                member);
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testCreateWithZeroQuantity() {
        try {
            Order zeoQuantityOrder = new Order(1L, option, 0L, LocalDateTime.now(), "임시 메시지",
                member);
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testCreateWithMinusQuantity() {
        try {
            Order minusQuantityOrder = new Order(1L, option, -10L, LocalDateTime.now(), "임시 메시지",
                member);
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

}