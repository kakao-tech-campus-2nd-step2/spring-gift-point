package gift.entity;

import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void 생성_성공() {
        Category category = new Category("테스트 카테고리", "#FFFFFF", "https://example.com/image.jpg", "카테고리 설명");

        assertNotNull(category);
        assertEquals("테스트 카테고리", category.getName());
        assertEquals("#FFFFFF", category.getColor());
        assertEquals("https://example.com/image.jpg", category.getImageUrl());
        assertEquals("카테고리 설명", category.getDescription());
    }

    @Test
    void 생성_실패_이름_null() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new Category(null, "#FFFFFF", "https://example.com/image.jpg", "카테고리 설명");
        });

        assertEquals(ErrorCode.INVALID_CATEGORY_NAME, exception.getErrorCode());
    }

    @Test
    void 생성_실패_이름_빈값() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new Category("", "#FFFFFF", "https://example.com/image.jpg", "카테고리 설명");
        });

        assertEquals(ErrorCode.INVALID_CATEGORY_NAME, exception.getErrorCode());
    }

    @Test
    void 생성_실패_색상_null() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new Category("테스트 카테고리", null, "https://example.com/image.jpg", "카테고리 설명");
        });

        assertEquals(ErrorCode.INVALID_CATEGORY_COLOR, exception.getErrorCode());
    }

    @Test
    void 생성_실패_색상_빈값() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new Category("테스트 카테고리", "", "https://example.com/image.jpg", "카테고리 설명");
        });

        assertEquals(ErrorCode.INVALID_CATEGORY_COLOR, exception.getErrorCode());
    }

    @Test
    void 생성_실패_이미지_URL_null() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new Category("테스트 카테고리", "#FFFFFF", null, "카테고리 설명");
        });

        assertEquals(ErrorCode.INVALID_IMAGE_URL, exception.getErrorCode());
    }

    @Test
    void 생성_실패_이미지_URL_빈값() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new Category("테스트 카테고리", "#FFFFFF", "", "카테고리 설명");
        });

        assertEquals(ErrorCode.INVALID_IMAGE_URL, exception.getErrorCode());
    }

    @Test
    void 업데이트_성공() {
        Category category = new Category("테스트 카테고리", "#FFFFFF", "https://example.com/image.jpg", "카테고리 설명");

        category.update("새 테스트 카테고리", "#000000", "https://example.com/new_image.jpg", "새 카테고리 설명");

        assertEquals("새 테스트 카테고리", category.getName());
        assertEquals("#000000", category.getColor());
        assertEquals("https://example.com/new_image.jpg", category.getImageUrl());
        assertEquals("새 카테고리 설명", category.getDescription());
    }

    @Test
    void 업데이트_실패_이름_null() {
        Category category = new Category("테스트 카테고리", "#FFFFFF", "https://example.com/image.jpg", "카테고리 설명");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            category.update(null, "#000000", "https://example.com/new_image.jpg", "새 카테고리 설명");
        });

        assertEquals(ErrorCode.INVALID_CATEGORY_NAME, exception.getErrorCode());
    }

    @Test
    void 업데이트_실패_이름_빈값() {
        Category category = new Category("테스트 카테고리", "#FFFFFF", "https://example.com/image.jpg", "카테고리 설명");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            category.update("", "#000000", "https://example.com/new_image.jpg", "새 카테고리 설명");
        });

        assertEquals(ErrorCode.INVALID_CATEGORY_NAME, exception.getErrorCode());
    }

    @Test
    void 업데이트_실패_색상_null() {
        Category category = new Category("테스트 카테고리", "#FFFFFF", "https://example.com/image.jpg", "카테고리 설명");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            category.update("새 테스트 카테고리", null, "https://example.com/new_image.jpg", "새 카테고리 설명");
        });

        assertEquals(ErrorCode.INVALID_CATEGORY_COLOR, exception.getErrorCode());
    }

    @Test
    void 업데이트_실패_색상_빈값() {
        Category category = new Category("테스트 카테고리", "#FFFFFF", "https://example.com/image.jpg", "카테고리 설명");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            category.update("새 테스트 카테고리", "", "https://example.com/new_image.jpg", "새 카테고리 설명");
        });

        assertEquals(ErrorCode.INVALID_CATEGORY_COLOR, exception.getErrorCode());
    }

    @Test
    void 업데이트_실패_이미지_URL_null() {
        Category category = new Category("테스트 카테고리", "#FFFFFF", "https://example.com/image.jpg", "카테고리 설명");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            category.update("새 테스트 카테고리", "#000000", null, "새 카테고리 설명");
        });

        assertEquals(ErrorCode.INVALID_IMAGE_URL, exception.getErrorCode());
    }

    @Test
    void 업데이트_실패_이미지_URL_빈값() {
        Category category = new Category("테스트 카테고리", "#FFFFFF", "https://example.com/image.jpg", "카테고리 설명");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            category.update("새 테스트 카테고리", "#000000", "", "새 카테고리 설명");
        });

        assertEquals(ErrorCode.INVALID_IMAGE_URL, exception.getErrorCode());
    }
}
