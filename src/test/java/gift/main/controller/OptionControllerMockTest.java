package gift.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.main.dto.OptionChangeQuantityRequest;
import gift.main.dto.OptionRequest;
import gift.main.dto.OptionResponse;
import gift.main.service.OptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class OptionControllerMockTest {

<<<<<<<HEAD
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectMapper objectMapper = new ObjectMapper();
=======
    private MockMvc mockMvc;
    private MockMvc mockMvc;
>>>>>>>upstream/jinseohyun1228

    @Mock
    private OptionService optionService;

    @InjectMocks
    private OptionController optionController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(optionController).build();
    }

    @Test
    void findAllOptionTest() throws Exception {
        List<OptionResponse> options = Arrays.asList(new OptionResponse(1L, "1", 100), new OptionResponse(2L, "2", 100));
        when(optionService.findAllOption(1L)).thenReturn(options);

        mockMvc.perform(get("/admin/product/1/options"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(options)));

        verify(optionService).findAllOption(1L);
    }

    @Test
    void addOptionTest() throws Exception {
        OptionRequest validRequest = new OptionRequest("New Option", 100);
        String requestJson = objectMapper.writeValueAsString(validRequest);

        mockMvc.perform(post("/admin/product/1/option")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Option added successfully"));

        verify(optionService).addOption(1L, validRequest);
    }

    @Test
    void updateOptionTest() throws Exception {
        OptionRequest validRequest = new OptionRequest("Updated Option", 100);
        String requestJson = objectMapper.writeValueAsString(validRequest);

        mockMvc.perform(put("/admin/product/1/option/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Option updated successfully"));

        verify(optionService).updateOption(1L, 2L, validRequest);
    }

    @Test
    void deleteOptionTest() throws Exception {
        mockMvc.perform(delete("/admin/product/1/option/3"))
                .andExpect(status().isOk())
                .andExpect(content().string("Option deleted successfully"));

        verify(optionService).deleteOption(1L, 3L);
    }

    @Test
    void removeOptionQuantityTest() throws Exception {
        OptionChangeQuantityRequest request = new OptionChangeQuantityRequest(10);
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/admin/product/1/option/2/quantity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Option Quantity changed successfully"));

        verify(optionService).removeOptionQuantity(2L, request);
    }

    @Test
    void SendInvalidOptionNameTest() throws Exception {
        OptionRequest invalidRequest = new OptionRequest("", 100);
        String requestJson = objectMapper.writeValueAsString(invalidRequest);

        mockMvc.perform(post("/admin/product/1/option")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void SendInvalidOptiontQuantityTest() throws Exception {
        OptionRequest invalidRequest = new OptionRequest("정상정상", 0);
        String requestJson = objectMapper.writeValueAsString(invalidRequest);

        mockMvc.perform(post("/admin/product/1/option")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

}
