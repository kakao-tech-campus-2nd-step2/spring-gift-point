package gift.controller.option;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.option.OptionRequest;
import gift.dto.option.OptionResponse;
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
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OptionService optionService;

    @Autowired
    private ObjectMapper objectMapper;

    private OptionResponse.Info optionResponse;
    private OptionResponse.InfoList optionResponseList;
    private OptionRequest.Create optionRequestCreate;
    private OptionRequest.Update optionRequestUpdate;

    @BeforeEach
    void setUp() {
        optionResponse = new OptionResponse.Info(1L, "Option1", 100);
        optionResponseList = new OptionResponse.InfoList(1, Collections.singletonList(optionResponse));
        optionRequestCreate = new OptionRequest.Create("New Option", 50);
        optionRequestUpdate = new OptionRequest.Update("Updated Option", 200);
    }

    @Test
    @DisplayName("옵션을 모두 가져오는 메서드 테스트")
    void testGetAllOptions() throws Exception {
        Mockito.when(optionService.getAllOptions()).thenReturn(optionResponseList);

        mockMvc.perform(get("/api/products/options"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.optionCount").value(optionResponseList.optionCount()))
                .andExpect(jsonPath("$.options[0].id").value(optionResponse.id()))
                .andExpect(jsonPath("$.options[0].name").value(optionResponse.name()))
                .andExpect(jsonPath("$.options[0].quantity").value(optionResponse.quantity()));
    }
    @Test
    @DisplayName("특정 상품의 옵션을 가져오는 메서드 테스트")
    void testGetAllOptionsFromGift() throws Exception {
        Mockito.when(optionService.getOptionsByGiftId(1L)).thenReturn(optionResponseList);

        mockMvc.perform(get("/api/products/1/options"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(optionResponse.id()))
                .andExpect(jsonPath("$[0].name").value(optionResponse.name()))
                .andExpect(jsonPath("$[0].quantity").value(optionResponse.quantity()));
    }

    @Test
    @DisplayName("상품에 옵션을 추가하는 메서드 테스트")
    void testAddOptionToGift() throws Exception {
        mockMvc.perform(post("/api/products/options/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(optionRequestCreate)))
                .andExpect(status().isOk())
                .andExpect(content().string("옵션이 상품에 추가되었습니다!"));

        Mockito.verify(optionService).addOptionToGift(1L, optionRequestCreate);
    }

    @Test
    @DisplayName("상품의 옵션을 수정하는 메서드 테스트")
    void testUpdateOptionToGift() throws Exception {
        mockMvc.perform(put("/api/products/1/options/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(optionRequestUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().string("1번 상품에서1번 옵션이 변경되었습니다!"));

        Mockito.verify(optionService).updateOptionToGift(1L, 1L, optionRequestUpdate);
    }

    @Test
    @DisplayName("상품의 옵션 수량을 차감하는 메서드 테스트")
    void testSubtractOptionToGift() throws Exception {
        mockMvc.perform(patch("/api/products/options/1/1")
                        .param("quantity", "10"))
                .andExpect(status().isOk())
                .andExpect(content().string("1번 상품에서1번 옵션 수량이10만큼 차감되었습니다!"));

        Mockito.verify(optionService).subtractOptionToGift(1L, 1L, 10);
    }

    @Test
    @DisplayName("상품에서 옵션을 삭제하는 메서드 테스트")
    void testDeleteOptionFromGift() throws Exception {
        mockMvc.perform(delete("/api/products/1/options/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(optionService).deleteOptionFromGift(1L, 1L);
    }
}