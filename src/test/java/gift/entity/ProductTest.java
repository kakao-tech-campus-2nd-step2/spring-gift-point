package gift.entity;

import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void 상품_생성_성공() {
        ProductName productName = new ProductName("상품 이름");
        Category category = new Category("카테고리 이름", "#FFFFFF", "https://example.com/image.jpg", "카테고리 설명");
        Product product = new Product(productName, 1000, "https://example.com/product.jpg", category);

        assertNotNull(product);
        assertEquals("상품 이름", product.getName().getValue());
        assertEquals(1000, product.getPrice());
        assertEquals("https://example.com/product.jpg", product.getImageUrl());
        assertEquals("카테고리 이름", product.getCategory().getName());
    }

    @Test
    void 상품_업데이트_성공() {
        ProductName productName = new ProductName("상품 이름");
        Category category = new Category("카테고리 이름", "#FFFFFF", "https://example.com/image.jpg", "카테고리 설명");
        Product product = new Product(productName, 1000, "https://example.com/product.jpg", category);

        ProductName newProductName = new ProductName("새 상품 이름");
        Category newCategory = new Category("새 카테고리 이름", "#000000", "https://example.com/new_image.jpg", "새 카테고리 설명");
        product.update(newProductName, 2000, "https://example.com/new_product.jpg", newCategory);

        assertEquals("새 상품 이름", product.getName().getValue());
        assertEquals(2000, product.getPrice());
        assertEquals("https://example.com/new_product.jpg", product.getImageUrl());
        assertEquals("새 카테고리 이름", product.getCategory().getName());
    }

    @Test
    void 상품_가격_실패_음수() {
        ProductName productName = new ProductName("상품 이름");
        Category category = new Category("카테고리 이름", "#FFFFFF", "https://example.com/image.jpg", "카테고리 설명");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new Product(productName, -1000, "https://example.com/product.jpg", category);
        });

        assertEquals(ErrorCode.INVALID_PRICE, exception.getErrorCode());
    }

    @Test
    void 상품_이미지_URL_실패_null() {
        ProductName productName = new ProductName("상품 이름");
        Category category = new Category("카테고리 이름", "#FFFFFF", "https://example.com/image.jpg", "카테고리 설명");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new Product(productName, 1000, null, category);
        });

        assertEquals(ErrorCode.INVALID_IMAGE_URL, exception.getErrorCode());
    }

    @Test
    void 상품_이미지_URL_실패_빈값() {
        ProductName productName = new ProductName("상품 이름");
        Category category = new Category("카테고리 이름", "#FFFFFF", "https://example.com/image.jpg", "카테고리 설명");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new Product(productName, 1000, "", category);
        });

        assertEquals(ErrorCode.INVALID_IMAGE_URL, exception.getErrorCode());
    }

    @Test
    void 상품_이미지_URL_실패_공백만() {
        ProductName productName = new ProductName("상품 이름");
        Category category = new Category("카테고리 이름", "#FFFFFF", "https://example.com/image.jpg", "카테고리 설명");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new Product(productName, 1000, "   ", category);
        });

        assertEquals(ErrorCode.INVALID_IMAGE_URL, exception.getErrorCode());
    }

    @Test
    void 상품_이름_생성_성공() {
        ProductName productName = new ProductName("상품 이름");

        assertNotNull(productName);
        assertEquals("상품 이름", productName.getValue());
    }

    @Test
    void 상품_이름_실패_null() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new ProductName(null);
        });

        assertEquals(ErrorCode.INVALID_NAME_SIZE, exception.getErrorCode());
    }

    @Test
    void 상품_이름_실패_빈값() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new ProductName("");
        });

        assertEquals(ErrorCode.INVALID_NAME_SIZE, exception.getErrorCode());
    }

    @Test
    void 상품_이름_실패_길이초과() {
        String longName = "a".repeat(16);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new ProductName(longName);
        });

        assertEquals(ErrorCode.INVALID_NAME_SIZE, exception.getErrorCode());
    }

    @Test
    void 상품_이름_실패_유효하지않은문자포함() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new ProductName("상품 이름!");
        });

        assertEquals(ErrorCode.INVALID_NAME_PATTERN, exception.getErrorCode());
    }

    @Test
    void 상품_이름_실패_공백만() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new ProductName("   ");
        });

        assertEquals(ErrorCode.INVALID_NAME_SIZE, exception.getErrorCode());
    }

    @Test
    void 상품_이름_실패_카카오포함() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new ProductName("카카오 상품");
        });

        assertEquals(ErrorCode.KAKAO_NAME_NOT_ALLOWED, exception.getErrorCode());
    }
}
