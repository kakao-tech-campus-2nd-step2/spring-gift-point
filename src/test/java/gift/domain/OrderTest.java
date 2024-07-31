package gift.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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
    void testSetMember() {
        Member newMember = new Member("new@example.com", "newpassword");
        order.setMember(newMember);
        assertEquals(newMember, order.getMember());
    }

    @Test
    void testSetOption() {
        Option newOption = new Option("New Option", 5, null);
        order.setOption(newOption);
        assertEquals(newOption, order.getOption());
    }

    @Test
    void testSetQuantity() {
        order.setQuantity(5);
        assertEquals(5, order.getQuantity());
    }

    @Test
    void testSetOrderDateTime() {
        LocalDateTime newDateTime = LocalDateTime.of(2024, 7, 26, 0, 0);
        order.setOrderDateTime(newDateTime);
        assertEquals(newDateTime, order.getOrderDateTime());
    }

    @Test
    void testSetMessage() {
        String newMessage = "Handle with extra care.";
        order.setMessage(newMessage);
        assertEquals(newMessage, order.getMessage());
    }
}
