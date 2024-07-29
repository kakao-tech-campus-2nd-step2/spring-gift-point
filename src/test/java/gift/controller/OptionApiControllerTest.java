package gift.controller;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.administrator.category.Category;
import gift.administrator.option.Option;
import gift.administrator.option.OptionApiController;
import gift.administrator.option.OptionDTO;
import gift.administrator.option.OptionService;
import gift.administrator.product.Product;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class OptionApiControllerTest {

    private final OptionService optionService = mock(OptionService.class);
    private MockMvc mvc;
    private ObjectMapper objectMapper;
    private OptionApiController optionApiController;
    private OptionDTO optionDTO;
    private OptionDTO optionDTO1;

    @BeforeEach
    void beforeEach(){
        optionApiController = new OptionApiController(optionService);
        mvc = MockMvcBuilders.standaloneSetup(optionApiController)
            .defaultResponseCharacterEncoding(UTF_8)
            .build();
        objectMapper = new ObjectMapper();
        Category category = new Category("상품권", null, null, null);
        Option option = new Option("L", 3, null);
        List<Option> options = new ArrayList<>(List.of(option));
        Product product = new Product("라이언", 1000, "image.jpg", category, options);
        optionDTO1 = new OptionDTO("XL", 5, 1L);
        option.setProduct(product);
        optionDTO = OptionDTO.fromOption(option);
    }

    @Test
    @DisplayName("모든 옵션 가져오기")
    void getAllOptions() throws Exception {
        //given
        given(optionService.getAllOptions()).willReturn(
            Arrays.asList(optionDTO, optionDTO1));

        //when
        ResultActions resultActions = mvc.perform(
            MockMvcRequestBuilders.get("/api/options")
                .contentType("application/json"));

        //then
        resultActions.andExpect(status().isOk())
            .andExpect(content().json(
                objectMapper.writeValueAsString(Arrays.asList(optionDTO, optionDTO1))));
    }

    @Test
    @DisplayName("모든 옵션 상품 아이디로 가져오기")
    void getAllOptionsByProductId() throws Exception {
        //given
        given(optionService.getAllOptionsByProductId(1L)).willReturn(
            Arrays.asList(optionDTO, optionDTO1));

        //when
        ResultActions resultActions = mvc.perform(
            MockMvcRequestBuilders.get("/api/products/1/options")
                .contentType("application/json"));

        //then
        resultActions.andExpect(status().isOk())
            .andExpect(content().json(
                objectMapper.writeValueAsString(Arrays.asList(optionDTO, optionDTO1))));
    }

    @Test
    @DisplayName("옵션 아이디로 옵션 삭제하기")
    void deleteOptionByOptionId() throws Exception {
        //given
        doNothing().when(optionService).deleteOptionByOptionId(1L);

        //when
        ResultActions resultActions = mvc.perform(
            MockMvcRequestBuilders.delete("/api/options/1")
                .contentType("application/json"));

        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("옵션 아이디로 옵션 삭제하기")
    void deleteOptionByOptionIdOptionNotExistsError() {
        //given
        doThrow(new IllegalArgumentException("없는 아이디입니다.")).when(optionService).deleteOptionByOptionId(1L);

        //when

        //then
        assertThatIllegalArgumentException().isThrownBy(
            () -> optionApiController.deleteOptionByOptionId(1L)).withMessage("없는 아이디입니다.");
    }
}
