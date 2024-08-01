package gift.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductTest {

    @Test
    public void testProductCreation() {
        Category category = new Category("Test Category");
        Product product = new Product("Test Product", 1000, "test.jpg", category);

        assertEquals("Test Product", product.getName());
        assertEquals(1000, product.getPrice());
        assertEquals("test.jpg", product.getImageUrl());
        assertEquals(category, product.getCategory());
    }

    @Test
    public void testOptionCreation() {
        Category category = new Category("Test Category");
        Product product = new Product("Test Product", 1000, "test.jpg", category);
        Option option = new Option("Test Option", 10, product);

        assertEquals("Test Option", option.getName());
        assertEquals(10, option.getQuantity());
        assertEquals(product, option.getProduct());
    }
}
