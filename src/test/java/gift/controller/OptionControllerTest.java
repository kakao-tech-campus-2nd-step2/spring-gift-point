package gift.controller;

import static gift.util.constants.OptionConstants.OPTION_NOT_FOUND;
import static gift.util.constants.OptionConstants.OPTION_REQUIRED;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.dto.option.OptionCreateRequest;
import gift.dto.option.OptionResponse;
import gift.dto.option.OptionUpdateRequest;
import gift.exception.option.OptionNotFoundException;
import gift.service.OptionService;
import gift.util.TokenValidator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OptionController.class)
public class OptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OptionService optionService;

    @MockBean
    private TokenValidator tokenValidator;

    private OptionResponse optionResponse;

    @BeforeEach
    public void setUp() {
        optionResponse = new OptionResponse(1L, "Test Option", 100, 1L);
    }

    @Test
    @DisplayName("상품 ID로 모든 옵션 조회")
    public void testGetOptionsByProductId() throws Exception {
        when(optionService.getOptionsByProductId(1L)).thenReturn(List.of(optionResponse));

        mockMvc.perform(get("/api/products/1/options"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Test Option"));
    }

    @Test
    @DisplayName("옵션 ID로 조회")
    public void testGetOptionById() throws Exception {
        when(optionService.getOptionById(1L, 1L)).thenReturn(optionResponse);

        mockMvc.perform(get("/api/products/1/options/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Test Option"));
    }

    @Test
    @DisplayName("존재하지 않는 옵션 ID로 조회")
    public void testGetOptionByIdNotFound() throws Exception {
        when(optionService.getOptionById(1L, 1L)).thenThrow(
            new OptionNotFoundException(OPTION_NOT_FOUND + 1));

        mockMvc.perform(get("/api/products/1/options/1"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value(OPTION_NOT_FOUND + 1));
    }

    @Test
    @DisplayName("상품에 옵션 추가")
    public void testAddOptionToProduct() throws Exception {
        when(optionService.addOptionToProduct(eq(1L), any(OptionCreateRequest.class))).thenReturn(
            optionResponse);

        mockMvc.perform(post("/api/products/1/options")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Test Option\", \"quantity\": 100}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Test Option"));
    }

    @Test
    @DisplayName("옵션 업데이트")
    public void testUpdateOption() throws Exception {
        when(optionService.updateOption(eq(1L), eq(1L), any(OptionUpdateRequest.class))).thenReturn(
            optionResponse);

        mockMvc.perform(put("/api/products/1/options/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Updated Option\", \"quantity\": 200}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Test Option"));
    }

    @Test
    @DisplayName("존재하지 않는 옵션 ID로 업데이트")
    public void testUpdateOptionNotFound() throws Exception {
        when(optionService.updateOption(eq(1L), eq(1L), any(OptionUpdateRequest.class))).thenThrow(
            new OptionNotFoundException(OPTION_NOT_FOUND + 1));

        mockMvc.perform(put("/api/products/1/options/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Updated Option\", \"quantity\": 200}"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value(OPTION_NOT_FOUND + 1));
    }

    @Test
    @DisplayName("옵션 삭제")
    public void testDeleteOption() throws Exception {
        doNothing().when(optionService).deleteOption(1L, 1L);

        mockMvc.perform(delete("/api/products/1/options/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("존재하지 않는 옵션 ID로 삭제")
    public void testDeleteOptionNotFound() throws Exception {
        doNothing().when(optionService).deleteOption(1L, 1L);
        doThrow(new OptionNotFoundException(OPTION_NOT_FOUND + 1)).when(optionService)
            .deleteOption(1L, 1L);

        mockMvc.perform(delete("/api/products/1/options/1"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value(OPTION_NOT_FOUND + 1));
    }

    @Test
    @DisplayName("옵션이 하나만 존재할 때 삭제")
    public void testDeleteOptionLastOption() throws Exception {
        doThrow(new IllegalArgumentException(OPTION_REQUIRED)).when(optionService)
            .deleteOption(1L, 1L);

        mockMvc.perform(delete("/api/products/1/options/1"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value(OPTION_REQUIRED));
    }
}
