package gift.option.presentation;

import gift.auth.TokenService;
import gift.exception.type.NotFoundException;
import gift.member.application.MemberService;
import gift.option.application.OptionService;
import gift.option.application.command.OptionSubtractQuantityCommand;
import gift.option.presentation.request.OptionSubtractQuantityRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OptionController.class)
class OptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OptionService optionService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private MemberService memberService;

    String token;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        token = "testToken";
    }

    @Test
    void 옵션_수량_차감_성공() throws Exception {
        // Given
        Long optionId = 1L;
        int quantity = 5;
        OptionSubtractQuantityRequest request = new OptionSubtractQuantityRequest(quantity);
        OptionSubtractQuantityCommand command = request.toCommand(optionId);

        // When & Then
        mockMvc.perform(post("/api/options/{id}/subtract", optionId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\": " + quantity + "}")
                )
                .andExpect(status().isOk());

        Mockito.verify(optionService, Mockito.times(1)).subtractOptionQuantity(command);
    }

    @Test
    void 옵션을_찾을수_없을때_예외_처리() throws Exception {
        // Given
        Long optionId = 1L;
        int quantity = 5;
        OptionSubtractQuantityRequest request = new OptionSubtractQuantityRequest(quantity);
        OptionSubtractQuantityCommand command = request.toCommand(optionId);

        Mockito.doThrow(new NotFoundException("해당 옵션이 존재하지 않습니다."))
                .when(optionService).subtractOptionQuantity(command);

        // When & Then
        mockMvc.perform(post("/api/options/{id}/subtract", optionId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\": " + quantity + "}")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("해당 옵션이 존재하지 않습니다."));

        Mockito.verify(optionService, Mockito.times(1)).subtractOptionQuantity(command);
    }
}
