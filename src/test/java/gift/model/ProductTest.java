package gift.model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gift.exception.InvalidInputValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductTest {

    private Category category;
    private Category testCategory;
    private Product product;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "교환권", "#007700", "임시 이미지", "임시 설명");
        testCategory = new Category(null, "테스트", "#770077", "테스트 이미지", "테스트 설명");
        product = new Product(1L, "상품", "100", category, "https://kakao");
    }

    @Test
    void testCreateValidProduct() {
        assertAll(
            () -> assertThat(product.getId()).isNotNull(),
            () -> assertThat(product.getName()).isEqualTo("상품"),
            () -> assertThat(product.getPrice()).isEqualTo("100"),
            () -> assertThat(product.getCategory().getId()).isEqualTo(1L),
            () -> assertThat(product.getImageUrl()).isEqualTo("https://kakao")
        );
    }

    @Test
    void testCreateWithNullName() {
        try {
            Product nullNameProduct = new Product(1L, null, "100", category, "https://kakao");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testCreateWithEmptyName() {
        try {
            Product emptyNameProduct = new Product(1L, "", "200", category, "https://kakao");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testCreateWithLengthName() {
        try {
            Product lengthNameProduct = new Product(1L, "aaaa aaaa aaaa a", "200", category,
                "https://kakao");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testCreateWithInvalidName() {
        try {
            Product invalidNameProduct = new Product(1L, ".", "100", category, "https://kakao");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testCreateWithKaKaoName() {
        try {
            Product kakaoNameProduct = new Product(1L, "카카오", "100", category, "https://kakao");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testCreateWithNullPrice() {
        try {
            Product nullPriceProduct = new Product(1L, "상품", null, category, "https://kakao");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testCreateWithEmptyPrice() {
        try {
            Product emptyPriceProduct = new Product(1L, "상품", "", category, "https://kakao");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testCreateWithInvalidPrice() {
        try {
            Product invalidPriceProduct = new Product(1L, "상품", "abcde", category, "https://kakao");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testCreateWithNullImageUrl() {
        try {
            Product nullImageUrlProduct = new Product(1L, "상품", "100", category, null);
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testCreateWithEmptyImageUrl() {
        try {
            Product emptyImageUrlProduct = new Product(1L, "상품", "100", category, "");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testCreateWithInvalidImageUrl() {
        try {
            Product invalidImageUrlProduct = new Product(1L, "상품", "100", category, "kbm");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testUpdateValidProduct() {
        product.updateProduct("테스트", "200", testCategory, "https://test");
        assertAll(
            () -> assertEquals(product.getId(), 1L),
            () -> assertEquals(product.getPrice(), "200"),
            () -> assertEquals(product.getCategory().getName(), "테스트"),
            () -> assertEquals(product.getImageUrl(), "https://test")
        );
    }

    @Test
    void testUpdateWithNullName() {
        try {
            product.updateProduct(null, "200", testCategory, "https://test");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testUpdateWithEmptyName() {
        try {
            product.updateProduct("", "200", testCategory, "https://test");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testUpdateWithLengthName() {
        try {
            product.updateProduct("test".repeat(300), "200", testCategory, "https://test");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testUpdateWithInvalidName() {
        try {
            product.updateProduct(".<>".repeat(300), "200", testCategory, "https://test");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testUpdateWithKaKaoName() {
        try {
            product.updateProduct("뱅크카카오뱅크", "200", testCategory, "https://test");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testUpdateWithNullPrice() {
        try {
            product.updateProduct("테스트", null, testCategory, "https://test");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testUpdateWithEmptyPrice() {
        try {
            product.updateProduct("테스트", "", testCategory, "https://test");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testUpdateWithInvalidPrice() {
        try {
            product.updateProduct("테스트", "가격", testCategory, "https://test");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testUpdateWithNullImageUrl() {
        try {
            product.updateProduct("테스트", "2000", testCategory, null);
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testUpdateWithEmptyImageUrl() {
        try {
            product.updateProduct("테스트", "2000", testCategory, "");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    void testUpdateWithInvalidImageUrl() {
        try {
            product.updateProduct("테스트", "2000", testCategory, "test");
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }
}