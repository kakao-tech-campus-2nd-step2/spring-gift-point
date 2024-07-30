package gift.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.product.ProductRequest;
import gift.dto.product.ProductResponse;
import gift.dto.option.OptionRequest;
import gift.service.product.ProductService;
import gift.service.option.OptionService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private OptionService optionService;

    private ProductRequest.Create giftRequest;

    private ProductResponse.Info productResponse;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        OptionRequest.Create option = new OptionRequest.Create("testOption", 1);
        List<OptionRequest.Create> optionList = Arrays.asList(option);
        giftRequest = new ProductRequest.Create("Test Gift", 1000, "test.jpg", 1L, optionList);
        productResponse = new ProductResponse.Info(1L, "Test Gift", 1000, "test.jpg");
        objectMapper = new ObjectMapper();

    }

    @Test
    @DisplayName("상품을 잘 추가하는지 테스트")
    void testAddGift() throws Exception {
        Mockito.when(productService.addGift(any(ProductRequest.Create.class))).thenReturn(productResponse);
        String giftRequestJson = objectMapper.writeValueAsString(giftRequest);
        mockMvc.perform(post("/api/gifts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(giftRequestJson))
                .andExpect(status().isCreated())
                .andExpect(content().string("Gift created"));

        Mockito.verify(productService).addGift(any(ProductRequest.Create.class));
    }

    @Test
    @DisplayName("옵션 없이 상품을 추가했을 때 오류 메시지가 잘 뜨는지 테스트")
    void testAddGiftNoOption() throws Exception {
        ProductRequest.Create invalidGiftRequest = new ProductRequest.Create("Test Gift", 1000, "test.jpg", 1L, null);
        String giftRequestJson = objectMapper.writeValueAsString(invalidGiftRequest);
        mockMvc.perform(post("/api/gifts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(giftRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.options").value("옵션은 최소 하나 이상 포함되어야 합니다."));
    }
    @Test
    @DisplayName("상품 업데이트 메서드가 잘 동작하는지 테스트")
    void testupdateGift() throws Exception {
        Mockito.doNothing().when(productService).updateGift(any(), anyLong());

        String giftRequestJson = objectMapper.writeValueAsString(giftRequest);

        mockMvc.perform(put("/api/gifts/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(giftRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().string("상품 수정이 완료되었습니다."));

        Mockito.verify(productService).updateGift(any(ProductRequest.Update.class), anyLong());
    }

    @Test
    @DisplayName("상품을 삭제하는 메서드가 잘 동작하는지 테스트")
    void testDeleteOptionFromGift() throws Exception {
        Mockito.doNothing().when(productService).deleteGift(anyLong());

        mockMvc.perform(delete("/api/gifts/{id}", 1L))
                .andExpect(status().isNoContent());
    }

}