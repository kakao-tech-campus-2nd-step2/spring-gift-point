package gift.product.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.product.service.ProductOptionService;
import gift.product.service.dto.ProductOptionInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductOptionController.class)
@ActiveProfiles("test")
class ProductOptionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductOptionService productOptionService;

    @Test
    @DisplayName("ProductOptionController Option생성 테스트")
    void createProductOptionTest() throws Exception {
        //given
        final Long productId = 1L;
        final String requestURI = "/api/products/" + productId + "/options";
        final String requestBody = "{\n" +
                "  \"name\": \"optionName\",\n" +
                "  \"quantity\": 1000\n" +
                "}";

        given(productOptionService.createProductOption(eq(productId), any())).willReturn(1L);

        //when//then
        mockMvc.perform(post(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("ProductOptionController Option수정 테스트")
    void modifyProductOptionTest() throws Exception {
        //given
        final Long productId = 1L;
        final Long optionId = 1L;
        final String requestURI = "/api/products/" + productId + "/options/" + optionId;
        final String requestBody = "{\n" +
                "  \"name\": \"optionName\",\n" +
                "  \"quantity\": 1000\n" +
                "}";

        //when//then
        mockMvc.perform(patch(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
        then(productOptionService).should().modifyProductOption(eq(productId), eq(optionId), any());
    }

    @Test
    @DisplayName("ProductOptionController Option조회 테스트")
    void getProductOptionTest() throws Exception {
        //given
        final Long productId = 1L;
        final Long optionId = 1L;
        ProductOptionInfo productOptionInfo = new ProductOptionInfo(1L, "optionName", 1000);
        final String requestURI = "/api/products/" + productId + "/options/" + optionId;

        given(productOptionService.getProductOptionInfo(eq(productId), eq(optionId))).willReturn(productOptionInfo);

        //when//then
        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.id").value(productOptionInfo.id()),
                        jsonPath("$.name").value(productOptionInfo.name()),
                        jsonPath("$.quantity").value(productOptionInfo.quantity())
                );
        then(productOptionService).should().getProductOptionInfo(eq(productId), eq(optionId));
    }
}