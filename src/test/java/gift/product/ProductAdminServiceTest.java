package gift.product;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.category.model.dto.Category;
import gift.category.service.CategoryService;
import gift.product.model.ProductRepository;
import gift.product.model.dto.option.CreateOptionRequest;
import gift.product.model.dto.product.CreateProductAdminRequest;
import gift.product.model.dto.product.Product;
import gift.product.service.OptionService;
import gift.product.service.ProductAdminService;
import gift.product.service.ProductService;
import gift.user.model.dto.AppUser;
import gift.user.model.dto.Role;
import gift.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductAdminServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @Mock
    private OptionService optionService;

    @InjectMocks
    private ProductAdminService productAdminService;

    private CreateProductAdminRequest createProductAdminRequest;
    private Category defaultCategory;
    private AppUser defaultSeller;
    private Product defaultProduct;

    @BeforeEach
    void setUp() {
        List<CreateOptionRequest> createRequest = List.of(new CreateOptionRequest("option", 10, 300));
        createProductAdminRequest = new CreateProductAdminRequest("ProductName", 100, "http://image.url", 1L, 1L,
                createRequest);
        defaultCategory = new Category("기본", "기본 카테고리");
        defaultSeller = new AppUser("aabb@kakao.com", "1234", Role.USER, "aaaa");
        defaultProduct = new Product("test", 100, "image", defaultSeller, defaultCategory);

    }

    @Test
    @DisplayName("상품 추가 시 상품이 저장되어야 한다.")
    void addProduct_ShouldSaveProduct_WhenRequestIsValid() {
        // given
        when(categoryService.getCategory(createProductAdminRequest.categoryId())).thenReturn(defaultCategory);
        when(userService.findUser(createProductAdminRequest.sellerId())).thenReturn(defaultSeller);
        doNothing().when(optionService).addOptionList(any(Product.class), any(List.class));

        // when, then
        assertDoesNotThrow(() -> productAdminService.addProduct(createProductAdminRequest));
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("상품 추가 요청에서 카테고리가 존재하지 않을 경우 EntityNotFoundException을 던진다.")
    void addProduct_ShouldThrowEntityNotFoundException_WhenCategoryDoesNotExist() {
        // given
        when(categoryService.getCategory(createProductAdminRequest.categoryId())).thenThrow(
                new EntityNotFoundException("Category"));

        // when, then
        assertThrows(EntityNotFoundException.class, () -> productAdminService.addProduct(createProductAdminRequest));
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    @DisplayName("상품 추가 요청에서 판매자 정보가 존재하지 않을 경우 EntityNotFoundException을 던진다.")
    void addProduct_ShouldThrowEntityNotFoundException_WhenSellerDoesNotExist() {
        // given
        when(categoryService.getCategory(createProductAdminRequest.categoryId())).thenReturn(defaultCategory);
        when(userService.findUser(createProductAdminRequest.sellerId())).thenThrow(
                new EntityNotFoundException("Seller"));

        // when, then
        assertThrows(EntityNotFoundException.class, () -> productAdminService.addProduct(createProductAdminRequest));
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    @DisplayName("상품과 카테고리가 존재할 때 상품 카테고리가 변경되어야 한다.")
    void updateCategory_ShouldUpdateProductCategory_WhenProductAndCategoryExist() {
        // given
        Long productId = 1L;
        Long newCategoryId = 2L;
        Category newCategory = new Category();
        when(productService.findProduct(productId)).thenReturn(defaultProduct);
        when(categoryService.getCategory(newCategoryId)).thenReturn(newCategory);

        // when, then
        assertDoesNotThrow(() -> productAdminService.updateCategory(productId, newCategoryId));
        verify(productRepository, times(1)).save(defaultProduct);
    }

    @Test
    @DisplayName("상품 카테고리 변경 요청 시 상품이 존재하지 않을 경우 EntityNotFoundException을 던진다.")
    void updateCategory_ShouldThrowEntityNotFoundException_WhenProductDoesNotExist() {
        // given
        Long productId = 1L;
        Long newCategoryId = 2L;
        when(productService.findProduct(productId)).thenThrow(new EntityNotFoundException("Product"));

        // when, then
        assertThrows(EntityNotFoundException.class, () -> productAdminService.updateCategory(productId, newCategoryId));
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    @DisplayName("상품 카테고리 변경 요청 시 카테고리가 존재하지 않을 경우 EntityNotFoundException을 던진다.")
    void updateCategory_ShouldThrowEntityNotFoundException_WhenCategoryDoesNotExist() {
        // given
        Long productId = 1L;
        Long newCategoryId = 2L;
        when(productService.findProduct(productId)).thenReturn(defaultProduct);
        when(categoryService.getCategory(newCategoryId)).thenThrow(new EntityNotFoundException("Category"));

        // when, then
        assertThrows(EntityNotFoundException.class, () -> productAdminService.updateCategory(productId, newCategoryId));
        verify(productRepository, times(0)).save(any(Product.class));
    }
}
