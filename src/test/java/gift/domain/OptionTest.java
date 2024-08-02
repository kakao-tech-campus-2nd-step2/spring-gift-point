package gift.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OptionTest {

    @Test
    public void testOptionCreation() {
        Category category = new Category();
        Product product = new Product("Test Product", 1000, "test.jpg", category);
        Option option = new Option("Test Option", 10, product);

        assertEquals("Test Option", option.getName());
        assertEquals(10, option.getQuantity());
        assertEquals(product, option.getProduct());
    }
}
