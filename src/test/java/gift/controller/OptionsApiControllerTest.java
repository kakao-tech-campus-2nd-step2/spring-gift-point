package gift.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.JwtTokenProvider;
import gift.exception.option.NotFoundOptionsException;
import gift.exception.product.NotFoundProductException;
import gift.model.Category;
import gift.model.Member;
import gift.model.Options;
import gift.model.Product;
import gift.model.Role;
import gift.request.OptionsRequest;
import gift.service.OptionsService;
import gift.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class OptionsApiControllerTest {

    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private OptionsService optionsService;

    private String token;

    @BeforeEach
    void setUp() {
        token = tokenProvider.generateToken(
            new Member("abc123@a.com", "1234", Role.ROLE_ADMIN));
    }

    @DisplayName("존재하는 상품에 옵션 생성 요청 테스트")
    @Test
    void add() throws Exception {
        //given
        OptionsRequest optionsRequest = demoRequest();
        Long optionId = 1L;
        Product product = demoProduct();
        Options options = demoOptions(optionId, product);
        String content = objectMapper.writeValueAsString(optionsRequest);

        given(optionsService.addOption(any(String.class), any(Integer.class), any(Long.class)))
            .willReturn(options);

        //when //then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/products/{id}/options", product.getId())
                    .header("Authorization", "Bearer " + token)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(print());
    }

    @DisplayName("존재하지 않는 상품에 옵션 생성 요청 테스트")
    @Test
    void failAdd() throws Exception {
        //given
        OptionsRequest optionsRequest = demoRequest();
        Long notExistedProductId = 1L;
        String content = objectMapper.writeValueAsString(optionsRequest);

        given(optionsService.addOption(any(String.class), any(Integer.class), any(Long.class)))
            .willThrow(NotFoundProductException.class);

        //when //then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/products/{id}/options", notExistedProductId)
                    .header("Authorization", "Bearer " + token)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    @DisplayName("옵션 수정 요청 테스트")
    @Test
    void update() throws Exception {
        //given
        OptionsRequest optionsRequest = new OptionsRequest("수정 옵션", 543);
        Long optionId = 1L;
        Product product = demoProduct();
        Options options = demoOptions(optionId, product);
        String content = objectMapper.writeValueAsString(optionsRequest);
        given(optionsService.updateOption(any(Long.class), any(String.class), any(Integer.class), any(Long.class)))
            .willReturn(options);

        //when //then
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/products/{id}/options", product.getId())
                    .param("option_id", String.valueOf(optionId))
                    .header("Authorization", "Bearer " + token)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(print());
    }

    @DisplayName("존재하지 않는 옵션에 대한 수정 요청 테스트")
    @Test
    void failUpdate() throws Exception {
        //given
        OptionsRequest optionsRequest = new OptionsRequest("수정 옵션", 543);
        Long optionId = 1L;
        Product product = demoProduct();
        String content = objectMapper.writeValueAsString(optionsRequest);
        given(optionsService.updateOption(any(Long.class), any(String.class), any(Integer.class), any(Long.class)))
            .willThrow(NotFoundOptionsException.class);

        //when //then
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/products/{id}/options", product.getId())
                    .param("option_id", String.valueOf(optionId))
                    .header("Authorization", "Bearer " + token)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    @DisplayName("옵션 삭제 요청 테스트")
    @Test
    void delete() throws Exception {
        //given
        Long optionId = 1L;
        Product product = demoProduct();
        doNothing().when(optionsService).deleteOption(optionId, product.getId());

        //when //then
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/products/{id}/options", product.getId())
                    .param("option_id", String.valueOf(optionId))
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(print());
    }


    private static OptionsRequest demoRequest() {
        return new OptionsRequest("옵션", 1);
    }

    private static Options demoOptions(Long id, Product product) {
        return new Options(id, "옵션", 1, product);
    }

    private static Product demoProduct() {
        return new Product(1L, "상품", 1000, "http://a.com", new Category(1L, "카테고리"));
    }


}