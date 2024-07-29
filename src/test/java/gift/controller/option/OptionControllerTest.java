package gift.controller.option;

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


    private OptionResponse optionResponse;


    @BeforeEach
    void setUp() {
        optionResponse = new OptionResponse(1L, "Option1", 100);
    }

    @Test
    @DisplayName("옵션을 가져오는 메서드 테스트")
    void testGetAllOptions() throws Exception {
        Mockito.when(optionService.getAllOptions()).thenReturn(Arrays.asList(optionResponse));

        mockMvc.perform(get("/api/gifts/options"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(optionResponse.getId()))
                .andExpect(jsonPath("$[0].name").value(optionResponse.getName()))
                .andExpect(jsonPath("$[0].quantity").value(optionResponse.getQuantity()));
    }

    @Test
    @DisplayName("옵션을 추가 메서드 테스트")
    void testAddOptionToGift() throws Exception {
        Mockito.doNothing().when(optionService).addOptionToGift(anyLong(), any());

        String optionRequestJson = "{ " +
                "\"name\": \"Option1\", " +
                "\"quantity\": 10 " +
                "}";

        mockMvc.perform(post("/api/gifts/options/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(optionRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().string("옵션이 상품에 추가되었습니다!"));

    }

    @Test
    @DisplayName("옵션을 업데이트하는 메서드 테스트")
    void testUpdateOptionToGift() throws Exception {
        Mockito.doNothing().when(optionService).updateOptionToGift(anyLong(), anyLong(), any());

        String optionRequestJson = "{ \"name\": \"UpdatedOption\"," +
                " \"quantity\": 15" +
                "}";

        mockMvc.perform(put("/api/gifts/options/{giftId}/{optionId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(optionRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().string("1번 상품에서1번 옵션이 변경되었습니다!"));
    }

    @Test
    @DisplayName("옵션을 삭제하는 메서드 테스트")
    void testDeleteOptionFromGift() throws Exception {
        Mockito.doNothing().when(optionService).deleteOptionFromGift(anyLong(), anyLong());

        mockMvc.perform(delete("/api/gifts/options/{giftId}/{optionId}", 1L, 1L))
                .andExpect(status().isNoContent())
                .andExpect(content().string("1번 상품에서1번 옵션이 삭제되었습니다!"));
    }

}