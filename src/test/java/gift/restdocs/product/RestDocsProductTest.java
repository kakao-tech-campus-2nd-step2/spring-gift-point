package gift.restdocs.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.JwtTokenProvider;
import gift.config.LoginWebConfig;
import gift.controller.ProductApiController;
import gift.model.Category;
import gift.model.Options;
import gift.model.Product;
import gift.paging.PagingService;
import gift.request.ProductAddRequest;
import gift.request.ProductUpdateRequest;
import gift.response.OptionResponse;
import gift.response.ProductOptionsResponse;
import gift.response.ProductResponse;
import gift.restdocs.AbstractRestDocsTest;
import gift.service.OptionsService;
import gift.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.request.RequestDocumentation;


@WebMvcTest(value = ProductApiController.class,
    excludeFilters = {@Filter(type = FilterType.ASSIGNABLE_TYPE, classes = LoginWebConfig.class)})
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
public class RestDocsProductTest extends AbstractRestDocsTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtTokenProvider tokenProvider;
    @MockBean
    private ProductService productService;
    @MockBean
    private OptionsService optionsService;
    @MockBean
    private PagingService pagingService;

    private String token = "{ACCESS_TOKEN}";


    @Test
    void getAllProducts() throws Exception {
        //given
        Product product = demoProduct(1L);
        int page = 1;
        int size = 10;
        String sort = "id";
        PageRequest pageRequest = PageRequest.of(page - 1, 10,
            Sort.by(Direction.ASC, sort));
        List<Product> products = new ArrayList<>();
        LongStream.range(1, 11)
            .forEach(i -> products.add(demoProduct(i)));
        Page<Product> pagedAllProducts = createPage(products, page, size);

        given(pagingService.makeProductsPageRequest(any(int.class), any(int.class), any(String.class)))
            .willReturn(pageRequest);
        given(productService.getPagedAllProducts(any(PageRequest.class)))
            .willReturn(pagedAllProducts);

        //when //then
        mockMvc.perform(get("/api/products?page=" + page + "&size=" + size + "&sort=" + sort)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andDo(document("rest-docs-product-test/get-all-products",
                queryParameters(
                    parameterWithName("page").description("page number"),
                    parameterWithName("size").description("number of products"),
                    parameterWithName("sort").description("sort option ex) id, name, quantity")
                )));
    }

    @Test
    void addProduct() throws Exception {
        //given
        ProductAddRequest addRequest = demoAddRequest();
        Long optionId = 1L;
        Product product = demoProduct(1L);
        String content = objectMapper.writeValueAsString(addRequest);

        given(productService.addProduct(any(String.class), any(Integer.class),
            any(String.class), any(String.class)))
            .willReturn(product);
        given(optionsService.addOption(any(String.class), any(Integer.class),
            any(Long.class)))
            .willReturn(demoOptions(optionId, product));

        //when //then
        mockMvc.perform(post("/api/products")
                .header("Authorization", "Bearer " + token)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(print());
    }

    @Test
    void updateProduct() throws Exception {
        //given
        Long productId = 1L;
        ProductUpdateRequest updateRequest = demoUpdateRequest();
        Long optionId = 1L;
        Category updatedCategory = new Category(1L, updateRequest.categoryName());
        Product updatedProduct = new Product(productId, updateRequest.name(),
            updateRequest.price(), updateRequest.imageUrl(), updatedCategory);
        String content = objectMapper.writeValueAsString(updateRequest);

        given(productService.updateProduct(any(Long.class), any(String.class), any(Integer.class),
            any(String.class), any(String.class)))
            .willReturn(updatedProduct);

        //when //then
        mockMvc.perform(
                RestDocumentationRequestBuilders.put("/api/products/{productId}", productId)
                    .header("Authorization", "Bearer " + token)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(document("rest-docs-product-test/update-product",
                pathParameters(
                    parameterWithName("productId").description("Product id")
                )));
    }

    @Test
    void deleteProduct() throws Exception {
        //given
        Long productId = 1L;
        doNothing().when(productService).deleteProduct(productId);
        doNothing().when(optionsService).deleteAllOptions(productId);

        //when //then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/products/{productId}", productId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(document("rest-docs-product-test/delete-product",
                pathParameters(
                    parameterWithName("productId").description("Product id")
                )));

        then(productService).should().deleteProduct(productId);
        then(optionsService).should().deleteAllOptions(productId);
    }

    private  <T> Page<T> createPage(List<T> content, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new PageImpl<>(content, pageable, content.size());
    }

    private ProductAddRequest demoAddRequest() {
        return new ProductAddRequest("상품1", 1000, "http://a.com",
            "교환권", "옵션A", 1);
    }

    private ProductUpdateRequest demoUpdateRequest() {
        return new ProductUpdateRequest( "수정된 상품명", 1500, "http://update.com",
            "수정된 카테고리명");
    }

    private Options demoOptions(Long id, Product product) {
        return new Options(id, "옵션" + id, 1, product);
    }

    private Product demoProduct(Long id) {
        return new Product(id, "상품" + id, 1000, "http://a.com",
            new Category(1L, "교환권"));
    }


}
