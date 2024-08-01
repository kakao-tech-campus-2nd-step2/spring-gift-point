package gift.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public class OrderTest {

    private Option option;
    private Order order;
    private final String timestamp = "2023-07-29 10:00:00";
    private final String message = "Test message";

    @BeforeEach
    void setUp() {
        option = new Option();
        option.setId(1);
        order = new Order(option, 2, timestamp, message);
    }

    @Test
    void testOrderCreation() {
        assertNotNull(order);
        assertEquals(option, order.getOption());
        assertEquals(2, order.getQuantity());
        assertEquals(timestamp, order.getTimestamp());
        assertEquals(message, order.getMessage());
    }

    @Test
    void testGetId() {
        // ID is typically set by the persistence provider, so we can't test its exact value
        assertEquals(0, order.getId());  // Assuming default value is 0 before persistence
    }

    @Test
    void testGetOption() {
        assertEquals(option, order.getOption());
        assertEquals(1, order.getOption().getId());
    }

    @Test
    void testGetQuantity() {
        assertEquals(2, order.getQuantity());
    }

    @Test
    void testGetTimestamp() {
        assertEquals(timestamp, order.getTimestamp());
    }

    @Test
    void testGetMessage() {
        assertEquals(message, order.getMessage());
    }
}