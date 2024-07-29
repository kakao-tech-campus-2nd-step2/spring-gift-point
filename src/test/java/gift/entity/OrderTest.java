package gift.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderTest {

    @Test
    void 주문_생성_성공() {
        // Given
        Product product = new Product(new ProductName("Test Product"), 1000, "https://example.com/image.jpg", new Category("Test Category", "#FF0000", "https://example.com/test.png", "Test Description"));
        Option option = new Option(new OptionName("Test Option"));
        ProductOption productOption = new ProductOption(product, option, 10);
        User user = new User("test@example.com", "password123");
        int quantity = 2;
        LocalDateTime orderDateTime = LocalDateTime.now();
        String message = "Test Message";

        // When
        Order order = new Order(productOption, user, quantity, orderDateTime, message);

        // Then
        assertNotNull(order);
        assertEquals(productOption, order.getProductOption());
        assertEquals(user, order.getUser());
        assertEquals(quantity, order.getQuantity());
        assertEquals(orderDateTime, order.getOrderDateTime());
        assertEquals(message, order.getMessage());
    }

    @Test
    void 주문_필드_접근_성공() {
        // Given
        Product product = new Product(new ProductName("Test Product"), 1000, "https://example.com/image.jpg", new Category("Test Category", "#FF0000", "https://example.com/test.png", "Test Description"));
        Option option = new Option(new OptionName("Test Option"));
        ProductOption productOption = new ProductOption(product, option, 10);
        User user = new User("test@example.com", "password123");
        int quantity = 2;
        LocalDateTime orderDateTime = LocalDateTime.now();
        String message = "Test Message";
        Order order = new Order(productOption, user, quantity, orderDateTime, message);

        // When
        Long id = order.getId();
        ProductOption retrievedProductOption = order.getProductOption();
        User retrievedUser = order.getUser();
        int retrievedQuantity = order.getQuantity();
        LocalDateTime retrievedOrderDateTime = order.getOrderDateTime();
        String retrievedMessage = order.getMessage();

        // Then
        assertEquals(productOption, retrievedProductOption);
        assertEquals(user, retrievedUser);
        assertEquals(quantity, retrievedQuantity);
        assertEquals(orderDateTime, retrievedOrderDateTime);
        assertEquals(message, retrievedMessage);
    }
}
