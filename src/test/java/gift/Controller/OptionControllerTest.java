package gift.Controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.DTO.CategoryDto;
import gift.DTO.OptionDto;
import gift.DTO.ProductDto;
import gift.Service.JwtService;
import gift.Service.OptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@WebMvcTest(OptionController.class)
class OptionControllerTest {

  @MockBean
  private OptionService optionService;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private JwtService jwtService;

  @Test
  void addOption() throws Exception {

    OptionDto optionDto = new OptionDto(1L, "옵션1", 2,
      new ProductDto(1L, "product1", 300, "fadsklf",
        new CategoryDto(1L, "교환권", "#6c95d1", "image_url", "교환권 카테고리")));

    String optionDtoJson = objectMapper.writeValueAsString(optionDto);
    mockMvc.perform(post("/api/options")
        .contentType(MediaType.APPLICATION_JSON)
        .content(optionDtoJson))
      .andExpect(status().isCreated());
  }

  @Test
  void getAllOptions() throws Exception {
    mockMvc.perform(get("/api/options")).andExpect(status().isOk());
  }

  @Test
  void getOptionById() throws Exception {
    Long productsId = 1L;
    mockMvc.perform(get("/api/products/{productId}/options", productsId))
      .andExpect(status().isOk());
  }

  @Test
  void deleteOption() throws Exception {
    Long productsId = 1L;
    mockMvc.perform(delete("/api/products/{productId}/options", productsId))
      .andExpect(status().isNoContent());
  }

  @Test
  void updateOption() throws Exception {
    Long id = 1L;
    OptionDto optionDto = new OptionDto(1L, "옵션2", 2,
      new ProductDto(1L, "product1", 300, "fadsklf",
        new CategoryDto(1L, "교환권", "#6c95d1", "image_url", "교환권 카테고리")));

    String optionDtoJson = objectMapper.writeValueAsString(optionDto);

    mockMvc.perform(put("/api/{optionsId}", id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(optionDtoJson))
      .andExpect(status().isOk());
  }
}