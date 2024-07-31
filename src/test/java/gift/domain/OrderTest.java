package gift.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrderTest {

    private Member member;
    private Option option;
    private Orders order;

    @BeforeEach
    void setUp() {
        member = new Member("test@example.com", "password");
        option = new Option("Test Option", 10, null);
        order = new Orders(member, option, 2, LocalDateTime.now(), "Please handle this order with care.");
    }

    @Test
    void testOrderCreation() {
        assertNotNull(order);
        assertEquals(member, order.getMember());
        assertEquals(option, order.getOption());
        assertEquals(2, order.getQuantity());
        assertNotNull(order.getOrderDateTime());
        assertEquals("Please handle this order with care.", order.getMessage());
    }

    @Test
    void testWithMember() {
        Member newMember = new Member("new@example.com", "newpassword");
        Orders newOrder = order.withMember(newMember);
        assertEquals(newMember, newOrder.getMember());
    }

    @Test
    void testWithOption() {
        Option newOption = new Option("New Option", 5, null);
        Orders newOrder = order.withOption(newOption);
        assertEquals(newOption, newOrder.getOption());
    }

    @Test
    void testWithQuantity() {
        Orders newOrder = order.withQuantity(5);
        assertEquals(5, newOrder.getQuantity());
    }

    @Test
    void testWithOrderDateTime() {
        LocalDateTime newDateTime = LocalDateTime.of(2024, 7, 26, 0, 0);
        Orders newOrder = order.withOrderDateTime(newDateTime);
        assertEquals(newDateTime, newOrder.getOrderDateTime());
    }

    @Test
    void testWithMessage() {
        String newMessage = "Handle with extra care.";
        Orders newOrder = order.withMessage(newMessage);
        assertEquals(newMessage, newOrder.getMessage());
    }
}
