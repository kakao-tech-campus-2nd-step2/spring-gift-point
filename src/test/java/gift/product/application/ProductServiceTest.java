package gift.product.application;

import gift.category.domain.Category;
import gift.category.domain.CategoryRepository;
import gift.exception.type.InvalidProductOptionException;
import gift.exception.type.KakaoInNameException;
import gift.exception.type.NotFoundException;
import gift.option.application.command.OptionCreateCommand;
import gift.option.application.command.OptionUpdateCommand;
import gift.option.domain.Option;
import gift.option.domain.OptionRepository;
import gift.product.application.command.ProductCreateCommand;
import gift.product.application.command.ProductUpdateCommand;
import gift.product.domain.Product;
import gift.product.domain.ProductRepository;
import gift.wishlist.domain.WishlistRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private OptionRepository optionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        reset(productRepository, categoryRepository, optionRepository, wishlistRepository);
    }

    @Test
    public void 모든_상품_페이징_조회_테스트() {
        // Given
        Category category = new Category(1L, "Category", "Color", "Description", "http://example.com/image.jpg");
        Product product1 = new Product("Product1", 1000, "http://example.com/image1.jpg", category);
        Product product2 = new Product("Product2", 2000, "http://example.com/image2.jpg", category);
        Page<Product> page = new PageImpl<>(List.of(product1, product2), PageRequest.of(0, 2), 2);
        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);

        Pageable pageable = PageRequest.of(0, 2);

        // When
        Page<ProductServiceResponse> products = productService.findAll(pageable);

        // Then
        assertThat(products.getTotalElements()).isEqualTo(2);
        assertThat(products.getContent()).hasSize(2);
        assertThat(products.getContent().get(0).name()).isEqualTo("Product1");
        assertThat(products.getContent().get(1).name()).isEqualTo("Product2");
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    public void 상품_ID로_조회_테스트() {
        // Given
        Product product = new Product("Product1", 1000, "http://example.com/image1.jpg", new Category("Category", "Color", "Description", "http://example.com/image.jpg"));
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));

        // When
        ProductServiceResponse productServiceResponse = productService.findById(1L);

        // Then
        assertThat(productServiceResponse.name()).isEqualTo("Product1");
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void 상품_ID로_조회_실패_테스트() {
        // Given
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> productService.findById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("해당 상품이 존재하지 않습니다.");
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void 상품_추가_테스트() {
        // Given
        Category category = new Category(1L, "Category", "Color", "Description", "http://example.com/image.jpg");
        OptionCreateCommand option1 = new OptionCreateCommand("Option1", 10);
        OptionCreateCommand option2 = new OptionCreateCommand("Option2", 20);
        ProductCreateCommand createCommand = new ProductCreateCommand(
                "Product1",
                1000,
                "http://example.com/image1.jpg",
                category.getId(),
                List.of(option1, option2)
        );

        Product product = createCommand.toProduct(category);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // When
        productService.save(createCommand);

        // Then
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void 상품_업데이트_테스트() {
        // Given
        Category category = new Category(1L, "Original Category", "Color", "Description", "http://example.com/image.jpg");
        Category newCategory = new Category(2L, "New Category", "Color", "Description", "http://example.com/image.jpg");
        Product product = new Product("Product1", 1000, "http://example.com/image1.jpg", category);
        Option option1 = new Option(1L, "Option1", 10);
        Option option2 = new Option(2L, "Option2", 20);
        product.addOption(option1);
        product.addOption(option2);
        OptionUpdateCommand newOption1 = new OptionUpdateCommand(1L, "newOption1", 100);
        OptionUpdateCommand newOption2 = new OptionUpdateCommand(2L, "newOption2", 200);
        ProductUpdateCommand updateCommand = new ProductUpdateCommand(
                1L,
                "UpdatedProduct",
                2000,
                "http://example.com/image2.jpg",
                newCategory.getId(),
                List.of(newOption1, newOption2)
        );

        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(newCategory));
        when(optionRepository.findById(1L)).thenReturn(Optional.of(option1));
        when(optionRepository.findById(2L)).thenReturn(Optional.of(option2));

        // When
        productService.update(updateCommand);

        // Then
        verify(productRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).findById(2L);
        assertThat(product.getName()).isEqualTo("UpdatedProduct");
        assertThat(product.getPrice()).isEqualTo(2000);
        assertThat(product.getImageUrl()).isEqualTo("http://example.com/image2.jpg");
        assertThat(product.getCategory()).isEqualTo(newCategory);
        assertThat(product.getOptions()).hasSize(2);
        assertThat(product.getOptions().get(0).getQuantity()).isEqualTo(100);
        assertThat(product.getOptions().get(1).getQuantity()).isEqualTo(200);
        assertThat(product.getOptions().get(0).getName()).isEqualTo("newOption1");
        assertThat(product.getOptions().get(1).getName()).isEqualTo("newOption2");
    }

    @Test
    public void 상품_업데이트_실패_테스트() {
        // Given
        Category category = new Category(1L, "Category", "Color", "Description", "http://example.com/image.jpg");
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        ProductUpdateCommand updateCommand = new ProductUpdateCommand(
                1L,
                "UpdatedProduct",
                2000,
                "http://example.com/image2.jpg",
                category.getId(),
                List.of()
        );

        // When & Then
        assertThatThrownBy(() -> productService.update(updateCommand))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("해당 상품이 존재하지 않습니다.");
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void 상품_삭제_테스트() {
        // Given
        Category category = new Category(1L, "Category", "Color", "Description", "http://example.com/image.jpg");
        Product product = new Product(1L, "Product1", 1000, "http://example.com/image1.jpg", category);
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
        doNothing().when(wishlistRepository).deleteAllByProductId(product.getId());
        doNothing().when(productRepository).delete(product);

        // When
        productService.delete(1L);

        // Then
        verify(productRepository, times(1)).findById(1L);
        verify(wishlistRepository, times(1)).deleteAllByProductId(product.getId());
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void 이름에카카오포함시_상품생성_실패_테스트() {
        // Given
        Category category = new Category(1L, "Category", "Color", "Description", "http://example.com/image.jpg");
        OptionCreateCommand option = new OptionCreateCommand("Option1", 10);
        ProductCreateCommand createCommand = new ProductCreateCommand(
                "카카오가 포함된 이름",
                1000,
                "http://example.com/image1.jpg",
                category.getId(),
                List.of(option)
        );
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // When & Then
        assertThatThrownBy(() -> productService.save(createCommand))
                .isInstanceOf(KakaoInNameException.class)
                .hasMessage("카카오가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.");
    }

    @Test
    public void 이름에카카오포함시_상품업데이트_실패_테스트() {
        // Given
        Category category = new Category(1L, "Category", "Color", "Description", "http://example.com/image.jpg");
        Product product = new Product("Product1", 1000, "http://example.com/image1.jpg", category);
        product.addOption(new Option(1L, "Option1", 10));
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        ProductUpdateCommand updateCommand = new ProductUpdateCommand(
                1L,
                "카카오가 포함된 이름",
                2000,
                "http://example.com/image2.jpg",
                category.getId(),
                List.of(new OptionUpdateCommand(1L, "Option1", 10))
        );

        when(optionRepository.findById(anyLong())).thenReturn(Optional.of(new Option(1L, "Option1", 10)));

        // When & Then
        assertThatThrownBy(() -> productService.update(updateCommand))
                .isInstanceOf(KakaoInNameException.class)
                .hasMessage("카카오가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.");
    }

    @Test
    void 옵션없는상품생성_실패_테스트() {
        // Given
        Category category = new Category(1L, "Category", "Color", "Description", "http://example.com/image.jpg");
        ProductCreateCommand createCommand = new ProductCreateCommand(
                "Product1",
                1000,
                "http://example.com/image1.jpg",
                category.getId(),
                List.of()
        );
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // When & Then
        assertThatThrownBy(() -> productService.save(createCommand))
                .isInstanceOf(InvalidProductOptionException.class)
                .hasMessage("상품은 최소 1개 이상의 옵션을 가져야 합니다.");
    }

    @Test
    void 옵션없는상품업데이트_실패_테스트() {
        // Given
        Category category = new Category(1L, "Category", "Color", "Description", "http://example.com/image.jpg");
        Product product = new Product("Product1", 1000, "http://example.com/image1.jpg", category);
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        ProductUpdateCommand updateCommand = new ProductUpdateCommand(
                1L,
                "UpdatedProduct",
                2000,
                "http://example.com/image2.jpg",
                category.getId(),
                List.of()
        );

        // When & Then
        assertThatThrownBy(() -> productService.update(updateCommand))
                .isInstanceOf(InvalidProductOptionException.class)
                .hasMessage("상품은 최소 1개 이상의 옵션을 가져야 합니다.");
    }

    @Test
    void 옵션이름이50이상인상품_추가_테스트() {
        // Given
        Category category = new Category(1L, "Category", "Color", "Description", "http://example.com/image.jpg");
        OptionCreateCommand option = new OptionCreateCommand("옵션이름이50자를초과하는경우옵션생성실패테스트옵션이름이50자를초과하는경우옵션생성실패테스트옵션이름이50자를초과", 10);
        ProductCreateCommand createCommand = new ProductCreateCommand(
                "Product1",
                1000,
                "http://example.com/image1.jpg",
                category.getId(),
                List.of(option)
        );
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // When & Then
        assertThatThrownBy(() -> productService.save(createCommand))
                .isInstanceOf(InvalidProductOptionException.class)
                .hasMessage("옵션 이름은 공백을 포함하여 최대 50자까지 입력 할 수 있습니다.");
    }
}
