package gift.wishes;

import gift.core.domain.product.Product;
import gift.core.domain.product.ProductCategory;
import gift.core.domain.product.ProductRepository;
import gift.core.domain.user.UserRepository;
import gift.core.domain.wishes.WishesRepository;
import gift.core.domain.wishes.WishesService;
import gift.core.domain.wishes.exception.WishAlreadyExistsException;
import gift.core.domain.wishes.exception.WishNotFoundException;
import gift.wishes.service.WishesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WishesServiceTests {

    @Mock
    private WishesRepository wishesRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    private WishesService wishesService;
    private Product sampleProduct;

    @BeforeEach
    public void setUp() {
        wishesService = new WishesServiceImpl(wishesRepository, productRepository, userRepository);
        sampleProduct = new Product(
                1L,
                "test",
                100,
                "test.jpg",
                sampleCategory()
        );
    }

    @Test
    public void testAddProductToWishes() {
        Long userId = 1L;

        when(productRepository.exists(1L)).thenReturn(true);
        when(wishesRepository.exists(1L, 1L)).thenReturn(false);

        wishesService.addProductToWishes(userId, sampleProduct);
        verify(wishesRepository).saveWish(userId, sampleProduct.id());
    }

    @Test
    @DisplayName("이미 존재하는 위시 상품을 추가 시도 테스트")
    public void testAddProductToWishesWithExistingWish() {
        Long userId = 1L;

        when(productRepository.exists(1L)).thenReturn(true);
        when(wishesRepository.exists(1L, 1L)).thenReturn(true);

        assertThrows(WishAlreadyExistsException.class, () -> wishesService.addProductToWishes(userId, sampleProduct));
    }

    @Test
    public void testRemoveProductFromWishes() {
        Long userId = 1L;
        when(wishesRepository.exists(1L, 1L)).thenReturn(true);

        wishesService.removeProductFromWishes(userId, sampleProduct);
        verify(wishesRepository).removeWish(userId, sampleProduct.id());
    }

    @Test
    @DisplayName("존재하지 않는 위시 상품을 삭제 시도 테스트")
    public void testRemoveProductFromWishesWithNonExistingWish() {
        Long userId = 1L;

        when(wishesRepository.exists(1L, 1L)).thenReturn(false);

        assertThrows(WishNotFoundException.class, () -> wishesService.removeProductFromWishes(userId, sampleProduct));
    }

    private static ProductCategory sampleCategory() {
        return ProductCategory.of("test", "#6c95d1", "test.jpg", "test");
    }
}
