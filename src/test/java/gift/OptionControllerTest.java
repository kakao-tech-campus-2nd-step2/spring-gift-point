package gift;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.controller.OptionController;
import gift.dto.OptionRequestDto;
import gift.dto.OptionResponseDto;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.service.OptionService;
import gift.service.ProductService;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
public class OptionControllerTest {

    @Mock
    private OptionService optionService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OptionController optionController;

    private MockMvc mockMvc;

    @Test
    @DisplayName("전건조회 테스트")
    public void getOptionsTest() throws Exception {
        OptionResponseDto optionResponseDto = new OptionResponseDto(1L, "교환권", 10);
        when(optionService.findAllByProductId(any(Long.class))).thenReturn(Arrays.asList(optionResponseDto));

        mockMvc = MockMvcBuilders.standaloneSetup(optionController).build();

        mockMvc.perform(get("/api/products/1/options"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("교환권"));
    }

    @Test
    @DisplayName("옵션추가 테스트")
    public void addOptionTest() throws Exception {
        OptionRequestDto optionRequestDto = new OptionRequestDto("교환권", 10);
        OptionResponseDto optionResponseDto = new OptionResponseDto(1L, "교환권", 10);
        when(productService.findById(anyLong())).thenReturn(
            Optional.of(new Product("치킨", 20000, "chicken.com", new Category("음식"))));
        when(optionService.save(any(Option.class))).thenReturn(new Option("교환권", 10, new Product("치킨", 20000, "chicken.com", new Category("음식"))));

        mockMvc = MockMvcBuilders.standaloneSetup(optionController).build();

        mockMvc.perform(post("/api/products/1/options")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"교환권\",\"quantity\":10}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("교환권"));
    }

}
