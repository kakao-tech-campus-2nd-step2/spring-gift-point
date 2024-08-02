package gift.option;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.controller.OptionController;
import gift.domain.Member.MemberRequest;
import gift.domain.Option.OptionRequest;
import gift.domain.Option.OptionResponse;
import gift.domain.Token;
import gift.service.MemberService;
import gift.service.OptionService;
import gift.util.JwtUtil;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OptionController.class)
public class OptionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OptionService optionService;
    @MockBean
    private MemberService memberService;
    @MockBean
    private JwtUtil jwtUtil;

    private ObjectMapper objectMapper;
    private String token;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        MemberRequest member = new MemberRequest("admin2@kakao.com", "2222");
        Token mockToken = new Token("mocktoken");

        given(memberService.login(member)).willReturn(mockToken);
        token = mockToken.getToken();

        given(jwtUtil.isTokenValid(token)).willReturn(true);

    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }

    @Test
    @DisplayName("옵션 전체 조회 기능 테스트")
    void testGetAllOption() throws Exception {
        //given
        Long productId = 1L;
        List<OptionResponse> responses = new ArrayList<>();

        OptionResponse response1 = new OptionResponse(1L,"옵션1", 1000L, 1L);
        OptionResponse response2 = new OptionResponse(2L,"옵션2", 2000L, 1L);

        responses.add(response1);
        responses.add(response2);

        given(optionService.getAllOptionByProductId(productId)).willReturn(responses);

        mockMvc.perform(get("/api/products/1/options")
                .headers(getHttpHeaders())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("옵션1"))
            .andExpect(jsonPath("$[1].name").value("옵션2"))
            .andExpect(content().json(objectMapper.writeValueAsString(responses)));

        then(optionService).should(times(1)).getAllOptionByProductId(productId);
    }

    @Test
    @DisplayName("옵션 단일 조회 테스트")
    public void testGetOptionById() throws Exception {
        //given
        Long productId = 1L;
        Long optionId = 1L;
        OptionResponse response = new OptionResponse(1L,"옵션1", 1000L, 1L);
        given(optionService.getOptionByProductIdAndId(productId, optionId)).willReturn(response);

        //when
        mockMvc.perform(get("/api/products/{productId}/options/{id}",productId,optionId)
                .headers(getHttpHeaders())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));

        then(optionService).should(times(1)).getOptionByProductIdAndId(productId,optionId);
    }

    @Test
    @DisplayName("옵션 추가 테스트")
    void testAddOption() throws Exception {
        // given
        Long productId = 1L;
        OptionRequest request = new OptionRequest("옵션3",3000L);

        // when
        mockMvc.perform(post("/api/products/1/options")
                .headers(getHttpHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());

        then(optionService).should(times(1)).addOption(productId,request);
    }

    @Test
    @DisplayName("옵션 업데이트 테스트")
    void testUpdateOption() throws Exception {
        // given
        Long productId = 1L;
        Long optionId = 3L;
        OptionRequest request = new OptionRequest("새옵션",3333L);

        // when
        mockMvc.perform(put("/api/products/{productId}/options/{id}",productId,optionId)
                .headers(getHttpHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());

        then(optionService).should(times(1)).updateOption(optionId, request);
    }

    @Test
    @DisplayName("옵션 삭제 테스트")
    void testDeleteOption() throws Exception {
        // given
        Long productId = 1L;
        Long optionId = 3L;
        // when
        mockMvc.perform(delete("/api/products/{productId}/options/{id}",productId,optionId)
                .headers(getHttpHeaders()))
            .andExpect(status().isOk());

        then(optionService).should(times(1)).deleteOption(optionId);
    }

}
