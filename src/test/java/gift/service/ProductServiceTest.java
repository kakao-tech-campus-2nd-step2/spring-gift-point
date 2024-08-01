package gift.service;

import gift.dto.request.AddProductRequest;
import gift.dto.request.OptionRequest;
import gift.dto.request.UpdateProductRequest;
import gift.dto.response.AddedOptionIdResponse;
import gift.dto.response.AddedProductIdResponse;
import gift.dto.response.OptionResponse;
import gift.dto.response.ProductResponse;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.ProductNotFoundException;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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

@ExtendWith(MockitoExtension.class)
@DisplayName("상품 서비스 단위테스트")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryService categoryService;
    @Mock
    private OptionService optionService;
    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품 추가(with Option)")
    void addProduct() {
        //Give
        AddProductRequest request = new AddProductRequest("productName", 100, "img", 1L, List.of(new OptionRequest("option", 1010)));
        Category category = new Category("CategoryName", "color", "description", "imageUrl");

        when(categoryService.getCategory(request.categoryId())).thenReturn(category);

        Product savedProduct = mock(Product.class);

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        when(savedProduct.getId()).thenReturn(1L);

        //When
        AddedProductIdResponse addedProductIdResponse = productService.addProduct(request);

        //Then
        assertThat(addedProductIdResponse.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("싱품에 옵션 추가하기(기존상황에서 추가하는것)")
    void addOptionToProduct() {
        //Given
        Long productId = 1L;
        Category category = new Category("CategoryName", "color", "description", "imageUrl");
        List<Option> options = List.of(new Option("option", 1010));
        Product product = new Product("product", 101, "img", category, options);

        OptionRequest optionRequest = new OptionRequest("newOption", 1010);
        Option option = Mockito.mock(Option.class);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(optionService.saveOption(any())).thenReturn(option);
        when(option.getId()).thenReturn(1L);

        //When
        AddedOptionIdResponse addedOptionIdResponse = productService.addOptionToProduct(productId, optionRequest);

        //Then
        assertThat(addedOptionIdResponse.optionId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("상품(Response) 얻기")
    void GetResponses() {
        //Given
        Pageable pageable = PageRequest.of(0, 10);

        Category category = new Category("CategoryName", "color", "description", "imageUrl");
        List<Option> options = List.of(new Option("option", 1010));
        Product product = new Product("product", 101, "img", category, options);

        Page<Product> page = new PageImpl<>(List.of(product));

        when(productRepository.findAll(pageable)).thenReturn(page);

        //When
        Page<ProductResponse> productResponses = productService.getProductResponses(pageable);

        //Then
        assertThat(productResponses.getContent())
                .hasSize(1)
                .first()
                .isInstanceOf(ProductResponse.class)
                .extracting("name", "price")
                .containsExactly("product", 101);
    }

    @Nested
    @DisplayName("상품(Entity) 얻기")
    class GetEntity {
        @Test
        @DisplayName("성공")
        void success() {
            //Given
            Category category = new Category("CategoryName", "color", "description", "imageUrl");
            List<Option> options = List.of(new Option("option", 1010));
            Product product = new Product("product", 101, "img", category, options);
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));

            //When
            Product resultProduct = productService.getProduct(1L);

            //Then
            assertThat(resultProduct.getName()).isEqualTo("product");
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 상품")
        void fail() {
            //Given
            when(productRepository.findById(1L)).thenReturn(Optional.empty());

            //When Then
            assertThatThrownBy(() -> productService.getProduct(1L))
                    .isInstanceOf(ProductNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("상품 수정")
    class Update {
        @Test
        @DisplayName("성공")
        void success() {
            //Given
            UpdateProductRequest request = new UpdateProductRequest("updateName", 10, "img", 1L);

            Category category = new Category("CategoryName", "color", "description", "imageUrl");
            List<Option> options = List.of(new Option("option", 1010));
            Product product = new Product("oldName", 101, "img", category, options);

            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(categoryService.getCategory(request.categoryId())).thenReturn(category);

            //When
            productService.updateProduct(request, 1L);

            //Then
            assertThat(product.getName()).isEqualTo("updateName");
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 상품")
        void fail() {
            //Given
            UpdateProductRequest request = new UpdateProductRequest("updateName", 10, "img", 1L);

            when(productRepository.findById(1L)).thenReturn(Optional.empty());

            //When Then
            assertThatThrownBy(() -> productService.updateProduct(request, 1L))
                    .isInstanceOf(ProductNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("상품 삭제")
    class Delete {
        @Test
        @DisplayName("성공")
        void success() {
            //Given
            Long productId = 1L;

            Category category = new Category("CategoryName", "color", "description", "imageUrl");
            List<Option> options = List.of(new Option("option", 1010));
            Product product = new Product("oldName", 101, "img", category, options);

            when(productRepository.findById(productId)).thenReturn(Optional.of(product));

            //When
            productService.deleteProduct(productId);

            //Then
            verify(productRepository, times(1)).delete(product);
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 상품")
        void fail() {
            //Given
            Long productId = 1L;

            when(productRepository.findById(productId)).thenReturn(Optional.empty());

            //When Then
            assertThatThrownBy(() -> productService.deleteProduct(productId))
                    .isInstanceOf(ProductNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("특정 상품 옵션(Response) 얻기")
    class OptionGet {
        @Test
        @DisplayName("성공")
        void success() {
            //Given
            Long productId = 1L;

            Category category = new Category("CategoryName", "color", "description", "imageUrl");
            List<Option> options = List.of(new Option("option", 1010));
            Product product = new Product("oldName", 101, "img", category, options);

            when(productRepository.findById(productId)).thenReturn(Optional.of(product));

            //When
            List<OptionResponse> optionResponses = productService.getOptionResponses(productId);

            //Then
            assertThat(optionResponses).first()
                    .isInstanceOf(OptionResponse.class)
                    .extracting("name", "quantity")
                    .containsExactly("option", 1010);

        }

        @Test
        @DisplayName("실패 - 존재하지 않는 상품")
        void fail() {
            //Given
            Long productId = 1L;

            when(productRepository.findById(productId)).thenReturn(Optional.empty());

            //When Then
            assertThatThrownBy(() -> productService.deleteProduct(productId))
                    .isInstanceOf(ProductNotFoundException.class);
        }
    }
}
