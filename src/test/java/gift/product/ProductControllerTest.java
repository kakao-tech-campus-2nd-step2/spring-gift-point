package gift.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import gift.option.Option;
import gift.option.OptionRequest;
import gift.option.OptionResponse;
import gift.option.OptionService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;
    @Mock
    private OptionService optionService;

    private MockMvc mockMvc;

    /*
    - [] get
    - [] post
        - [] 옵션 이름에 사용할 수 없는 특수 문자가 있는 경우
        - [] 옵션 이름이 50자를 넘어 가는 경우
        - [] 옵션 이름이 없는 경우
        - [] 옵션 수량이 1억개를 넘어 가는 경우
    - [] put
    - [] delete
     */


    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void getOptions() throws Exception {
        //given
        List<Option> options = new ArrayList<>();
        options.add(new Option(1L, "op1", 1, product()));
        when(optionService.findAllByProductId(any())).thenReturn(options);

        //when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/products/1/options")
        );

        //then
        MvcResult mvcResult = resultActions.andExpect(status().isOk())
            .andExpect(content().json(new Gson().toJson(options)))
            .andDo(print())
            .andReturn();
    }

    @Test
    void addOption() throws Exception {
        //given
        List<OptionResponse> options = new ArrayList<>();
        OptionRequest optionRequest = new OptionRequest(1L, "option", 10);
        Option option = option(1L, product());
        when(optionService.addOption(any(), any())).thenReturn(option);
        options.add(new OptionResponse(option));
        when(productService.addOption(any(), any(Option.class))).thenReturn(options);

        //when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/products/1/options")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(optionRequest))
        );

        //then
        MvcResult mvcResult = resultActions.andExpect(status().isCreated())
            .andExpect(content().json(new Gson().toJson(options)))
            .andDo(print())
            .andReturn();
    }

    @DisplayName("옵션 이름에 사용할 수 없는 특수 문자가 있는 경우")
    @Test
    void notValidSpecialCharacterInOptionName() throws Exception {
        //given
        String notValidName = "@$#name";
        OptionRequest optionRequest = new OptionRequest(null, notValidName, 1);
        //when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/products/1/options")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(optionRequest))
        );
        // then
        MvcResult mvcResult = resultActions.andExpect(status().isBadRequest())
            //.andExpect(jsonPath("$.message").value("( ), [ ], +, -, &, /, _ 를 제외한 특수문자는 사용할 수 없습니다."))
            .andDo(print())
            .andReturn();
    }

    @DisplayName("옵션 이름이 50자를 넘어 가는 경우")
    @Test
    void up50CharactersOptionName() throws Exception {
        //given
        String notValidName = "name".repeat(20);
        OptionRequest optionRequest = new OptionRequest(null, notValidName, 1);
        //when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/products/1/options")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(optionRequest))
        );
        // then
        MvcResult mvcResult = resultActions.andExpect(status().isBadRequest())
            .andDo(print())
            .andReturn();
    }

    @DisplayName("옵션 이름이 없는 경우")
    @Test
    void noOptionName() throws Exception {
        //given
        String notValidName = "";
        OptionRequest optionRequest = new OptionRequest(null, notValidName, 1);
        //when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/products/1/options")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(optionRequest))
        );
        // then
        MvcResult mvcResult = resultActions.andExpect(status().isBadRequest())
            .andDo(print())
            .andReturn();
    }

    @DisplayName("옵션 수량이 1억개를 넘어 가는 경우")
    @Test
    void up100millionOptionQuantity() throws Exception {
        //given
        int notValidQuantity = 100_000_001;
        OptionRequest optionRequest = new OptionRequest(null, "name", notValidQuantity);
        //when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/products/1/options")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(optionRequest))
        );
        // then
        MvcResult mvcResult = resultActions.andExpect(status().isBadRequest())
            .andDo(print())
            .andReturn();
    }

    @Test
    void updateOption() throws Exception {
        //given
        OptionRequest optionRequest = new OptionRequest(1L, "option", 10);

        //when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.put("/api/products/1/options/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(optionRequest))
        );

        //then
        MvcResult mvcResult = resultActions.andExpect(status().isCreated())
            .andExpect(content().string("update"))
            .andDo(print())
            .andReturn();
    }

    @Test
    void deleteOption() throws Exception {
        //given//when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/products/1/options/1")
        );

        //then
        MvcResult mvcResult = resultActions.andExpect(status().isOk())
            .andExpect(content().string("[]"))
            .andDo(print())
            .andReturn();
    }

    private Product product(){
        return new Product(1L, "product",1,"image", 1L);
    }
    private Option option(Long optionId, Product product) {
        return new Option(optionId, "option", 1, product);
    }

}
