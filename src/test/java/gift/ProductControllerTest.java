package gift;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.controller.ProductController;
import gift.dto.OptionRequestDto;
import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.ProductUpdateRequestDto;
import gift.entity.Category;
import gift.entity.Product;
import gift.service.CategoryService;
import gift.service.ProductService;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    @DisplayName("전건조회 테스트")
    public void getProductsTest() throws Exception {
        ProductResponseDto productResponseDto = new ProductResponseDto(1L, "치킨", 1000, "chicken.com", 1L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductResponseDto> productPage = new PageImpl<>(Arrays.asList(productResponseDto));
        when(productService.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Arrays.asList(new Product("치킨", 1000, "chicken.com", new Category("교환권")))));

        mockMvc.perform(get("/api/products")
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("치킨"));
    }

    @Test
    @DisplayName("상품추가")
    public void addProductTest() throws Exception {
        OptionRequestDto optionRequestDto = new OptionRequestDto("교환권", 10);
        ProductRequestDto productRequestDto = new ProductRequestDto("치킨", 1000, "chicken.com", 1L, Arrays.asList(optionRequestDto));
        Product product = new Product("치킨", 1000, "chicken.com", new Category("교환권"));
        when(productService.addProduct(any(ProductRequestDto.class))).thenReturn(product);
        when(categoryService.findById(any(Long.class))).thenReturn(Optional.of(new Category("교환권")));

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"치킨\",\"price\":1000,\"imageUrl\":\"chicken.com\",\"categoryId\":1,\"options\":[{\"name\":\"교환권\",\"quantity\":10}]}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("치킨"));

    }

    @Test
    @DisplayName("상품수정")
    public void updateProductTest() throws Exception {
        OptionRequestDto optionRequestDto = new OptionRequestDto("교환권", 10);
        ProductUpdateRequestDto productUpdateRequestDto = new ProductUpdateRequestDto("피자", 1500, "pizza.com", 1L);
        Product product = new Product("피자", 1500, "pizza.com", new Category("교환권"));
        when(productService.updateProduct(anyLong(), any(ProductUpdateRequestDto.class))).thenReturn(product);
        when(categoryService.findById(any(Long.class))).thenReturn(Optional.of(new Category("교환권")));

        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"피자\",\"price\":1500,\"imageUrl\":\"pizza.com\",\"categoryId\":1}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("피자"));
    }


    @Test
    public void deleteProductTest() throws Exception {
        when(productService.deleteProduct(any(Long.class))).thenReturn(1L);

        mockMvc.perform(delete("/api/products/1"))
            .andExpect(status().isOk());

    }
}

