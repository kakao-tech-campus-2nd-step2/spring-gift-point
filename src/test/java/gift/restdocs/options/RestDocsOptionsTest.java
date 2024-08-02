package gift.restdocs.options;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.JwtTokenProvider;
import gift.auth.OAuthService;
import gift.config.LoginWebConfig;
import gift.controller.OptionsApiController;
import gift.model.Category;
import gift.model.Options;
import gift.model.Product;
import gift.request.OptionsRequest;
import gift.response.OptionResponse;
import gift.response.ProductOptionsResponse;
import gift.response.ProductResponse;
import gift.restdocs.AbstractRestDocsTest;
import gift.service.OptionsService;
import gift.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(value = OptionsApiController.class,
    excludeFilters = {@Filter(type = FilterType.ASSIGNABLE_TYPE, classes = LoginWebConfig.class)})
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
public class RestDocsOptionsTest extends AbstractRestDocsTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtTokenProvider tokenProvider;
    @MockBean
    private OAuthService oAuthService;
    @MockBean
    private OptionsService optionsService;
    @MockBean
    private ProductService productService;

    private String token = "{ACCESS_TOKEN}";

    @Test
    void getProductWithAllOptions() throws Exception {
        //given
        Product product = demoProduct(1L);
        Options option = demoOptions(1L, product);
        Options option2 = demoOptions(2L, product);
        List<OptionResponse> options = new ArrayList<>();
        options.add(new OptionResponse(option.getId(), option.getName(), option.getQuantity()));
        options.add(new OptionResponse(option2.getId(), option2.getName(), option2.getQuantity()));

        ProductResponse productResponse = ProductResponse.createProductResponse(product);
        ProductOptionsResponse response = new ProductOptionsResponse(options);
        given(productService.getProduct(any(Long.class)))
            .willReturn(product);
        given(optionsService.getAllProductOptions(any(Long.class)))
            .willReturn(response);

        //when //then
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/products/{productId}/options",
                        product.getId())
                    .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andDo(document("rest-docs-options-test/get-product-with-all-options",
                requestHeaders(
                    headerWithName("Authorization").description("service access token")
                ),
                pathParameters(
                    parameterWithName("productId").description("Product id")
                )));
    }

    @Test
    void addOptions() throws Exception {
        //given
        OptionsRequest optionsRequest = demoRequest();
        Long optionId = 1L;
        Product product = demoProduct(1L);
        Options options = demoOptions(optionId, product);
        String content = objectMapper.writeValueAsString(optionsRequest);

        given(optionsService.addOption(any(String.class), any(Integer.class), any(Long.class)))
            .willReturn(options);

        //when //then
        mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/products/{productId}/options",
                        product.getId())
                    .header("Authorization", "Bearer " + token)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(document("rest-docs-options-test/add-options",
                requestHeaders(
                    headerWithName("Authorization").description("service access token")
                ),
                pathParameters(
                    parameterWithName("productId").description("Product id")
                )));
    }

    @Test
    void updateOptions() throws Exception {
        //given
        OptionsRequest optionsRequest = new OptionsRequest("수정 옵션", 543);
        Long optionId = 1L;
        Product product = demoProduct(1L);
        Options options = demoOptions(optionId, product);
        String content = objectMapper.writeValueAsString(optionsRequest);
        given(optionsService.updateOption(any(Long.class), any(String.class), any(Integer.class),
            any(Long.class)))
            .willReturn(options);

        //when //then
        mockMvc.perform(
                RestDocumentationRequestBuilders.put("/api/products/{productId}/options/{optionId}",
                        product.getId(), options.getId())
                    .header("Authorization", "Bearer " + token)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("rest-docs-options-test/update-options",
                requestHeaders(
                    headerWithName("Authorization").description("service access token")
                ),
                pathParameters(
                    parameterWithName("productId").description("Product id"),
                    parameterWithName("optionId").description("Option id")
                )));
    }

    @Test
    void deleteOptions() throws Exception {
        //given
        Long optionId = 1L;
        Product product = demoProduct(1L);
        doNothing().when(optionsService).deleteOption(optionId, product.getId());

        //when //then
        mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/api/products/{productId}/options/{optionId}",
                        product.getId(), optionId)
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(document("rest-docs-options-test/delete-options",
                requestHeaders(
                    headerWithName("Authorization").description("service access token")
                ),
                pathParameters(
                    parameterWithName("productId").description("Product id"),
                    parameterWithName("optionId").description("Option id")
                )));
    }


    private static OptionsRequest demoRequest() {
        return new OptionsRequest("옵션", 1);
    }

    private static Options demoOptions(Long id, Product product) {
        return new Options(id, "옵션" + id, 1, product);
    }

    private static Product demoProduct(Long id) {
        return new Product(id, "상품" + id, 1000, "http://a.com", new Category(1L, "카테고리"));
    }

}
