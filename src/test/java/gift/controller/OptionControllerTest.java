package gift.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.dto.betweenClient.member.MemberDTO;
import gift.service.MemberService;
import gift.service.OptionService;
import gift.util.JwtUtil;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = OptionController.class)
@Import(JwtUtil.class)
class OptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    OptionService optionService;

    @MockBean
    MemberService memberService;

    @Autowired
    JwtUtil jwtUtil;

    String token;

    @BeforeEach
    void setUp() {
        token = "Bearer " + jwtUtil.generateToken(new MemberDTO("1234@1234.com", "1234", "basic"));
    }

    @Test
    void getOneProductIdAllOptions() throws Exception {
        given(optionService.getOneProductIdAllOptions(any())).willReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/1/options").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void addOption() throws Exception {
        String requestBody = """
                {
                    "productId" : 1,
                    "name" : "옵션",
                    "quantity" : 1234
                }
                """;

        doNothing().when(optionService).addOption(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/1/options")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateOption() throws Exception {
        String requestBody = """
                {
                    "productId" : 1,
                    "name" : "옵션",
                    "quantity" : 1234
                }
                """;

        doNothing().when(optionService).updateOption(any(), any(), any());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/products/1/options/1")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void deleteOption() throws Exception {
        doNothing().when(optionService).deleteOption(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/1/options/1")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}