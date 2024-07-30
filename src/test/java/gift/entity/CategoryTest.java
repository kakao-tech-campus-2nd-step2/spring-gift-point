package gift.entity;

import gift.dto.request.ProductRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;


class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category("밥솥", "검은색", "압력밥솥", "www");
    }

    @Test
    void testCreateCategory() {
        assertEquals("밥솥", category.getName());
        assertEquals("검은색", category.getColor());
        assertEquals("압력밥솥", category.getDescription());
        assertEquals("www", category.getImageUrl());
        assertTrue(category.getProducts()
                .isEmpty());
    }

    @Test
    void testAddProduct() {
        Product product = new Product("드라이기", 1000, "유사 다이슨", category);
        category.addProduct(product);
        assertEquals(1, category.getProducts()
                .size());
        assertEquals(product, category.getProducts()
                .get(0));
        assertEquals(category, product.getCategory());
    }

    @Test
    void testAddProductWithDTO() {
        ProductRequestDTO productRequestDTO = Mockito.mock(ProductRequestDTO.class);
        Mockito.when(productRequestDTO.name()).thenReturn("다리미");
        Mockito.when(productRequestDTO.price()).thenReturn(1200);
        Mockito.when(productRequestDTO.imageUrl()).thenReturn("wwww");

        category.addProduct(productRequestDTO);
        assertEquals(1, category.getProducts()
                .size());
        Product addedProduct = category.getProducts()
                .get(0);
        assertEquals("다리미", addedProduct.getName());
        assertEquals(1200, addedProduct.getPrice());
        assertEquals("wwww", addedProduct.getImageUrl());
        assertEquals(category, addedProduct.getCategory());
    }


}
