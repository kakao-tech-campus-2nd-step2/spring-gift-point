package gift.unitTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.WebConfig;
import gift.controller.option.OptionController;
import gift.controller.option.OptionRequest;
import gift.controller.option.OptionResponse;
import gift.service.OptionService;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(OptionController.class)
class OptionControllerTest {

    @MockBean
    private OptionService optionService;

    @MockBean
    WebConfig webConfig;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void getAllOptionsTest() throws Exception {
    }

    @Test
    void getAllOptionsByProductIdTest() throws Exception {
    }

    @Test
    void getOptionTest() throws Exception {
    }

    @Test
    void createOptionTest() throws Exception {
    }

    @Test
    void updateOptionTest() throws Exception {
    }

    @Test
    void subtractOptionTest() throws Exception {
    }

    @Test
    void deleteOptionTest() throws Exception {
    }

    @DisplayName("옵션 생성 테스트0 : 성공")
    @Test
    void createOptionTest0() throws Exception {
        // given
        UUID productId = UUID.randomUUID();
        OptionRequest request = new OptionRequest("validName", 10);
        OptionResponse response = new OptionResponse(UUID.randomUUID(), "validName", 10, productId);
        doReturn(response).when(optionService).save(any(UUID.class), any(OptionRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/options/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        resultActions.andExpect(status().isCreated())
            .andExpect(jsonPath("id", response.id()).exists())
            .andExpect(jsonPath("name", response.name()).exists())
            .andExpect(jsonPath("quantity", response.quantity()).exists())
            .andExpect(jsonPath("productId", response.productId()).exists());
    }


//    @Test
    void subtractOption() throws Exception {
        // given
        UUID optionId = UUID.randomUUID();
        Integer quantity = 10;
        OptionResponse response = new OptionResponse(UUID.randomUUID(), "validName", quantity - 1,
            optionId);
        doReturn(response).when(optionService).subtract(optionId, quantity);

        // when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.put("/api/options/" + optionId + "/subtract/" + quantity)
//                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(response))
        );

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("id", response.id()).exists())
            .andExpect(jsonPath("name", response.name()).exists())
            .andExpect(jsonPath("quantity", response.quantity()).exists())
            .andExpect(jsonPath("productId", response.productId()).exists());
    }

    @Test
    void deleteOption() {
    }
}