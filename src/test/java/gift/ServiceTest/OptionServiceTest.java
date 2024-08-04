package gift.ServiceTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import gift.DTO.CategoryDto;
import gift.DTO.OptionDto;
import gift.DTO.ProductDto;
import gift.Service.OptionService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

@ExtendWith(MockitoExtension.class)
public class OptionServiceTest {

  @Mock
  OptionService optionService;

  @Test
  void addOptionTest() {
    OptionDto optionDto = new OptionDto(1L, "옵션1", 2,
      new ProductDto(1L, "product1", 300, "fadsklf",
        new CategoryDto(1L, "교환권", "#6c95d1", "image_url", "교환권 카테고리")));

    when(optionService.addOption(optionDto)).thenReturn(optionDto);
    OptionDto result = optionService.addOption(optionDto);

    assertEquals(optionDto, result);
  }

  @Test
  void getAllOptionsTest() {
    List<OptionDto> optionDtos = new ArrayList<>();

    OptionDto optionDto1 = new OptionDto(1L, "옵션1", 2,
      new ProductDto(1L, "product1", 300, "fadsklf",
        new CategoryDto(1L, "교환권", "#6c95d1", "image_url", "교환권 카테고리")));

    OptionDto optionDto2 = new OptionDto(2L, "옵션2", 2,
      new ProductDto(2L, "product2", 300, "fadsklf",
        new CategoryDto(2L, "교환권2", "#6c95d1", "image_url", "교환권 카테고리")));

    optionDtos.add(optionDto1);
    optionDtos.add(optionDto2);

    when(optionService.getAllOptions()).thenReturn(optionDtos);

    List<OptionDto> optionDtosByOptionService = optionService.getAllOptions();

    assertThat(optionDtosByOptionService.get(0).getId()).isEqualTo(optionDtos.get(0).getId());
    assertThat(optionDtosByOptionService.get(0).getName()).isEqualTo(optionDtos.get(0).getName());
    assertThat(optionDtosByOptionService.get(0).getQuantity()).isEqualTo(
      optionDtos.get(0).getQuantity());

    assertThat(optionDtosByOptionService.get(1).getId()).isEqualTo(optionDtos.get(1).getId());
    assertThat(optionDtosByOptionService.get(1).getName()).isEqualTo(optionDtos.get(1).getName());
    assertThat(optionDtosByOptionService.get(1).getQuantity()).isEqualTo(
      optionDtos.get(1).getQuantity());
  }

  @Test
  void getOptionByIdTest() {
    Long productId = 1L;

    OptionDto optionDto1 = new OptionDto(1L, "옵션1", 2,
      new ProductDto(1L, "product1", 300, "fadsklf",
        new CategoryDto(1L, "교환권", "#6c95d1", "image_url", "교환권 카테고리")));

    List<OptionDto> optionDtos = new ArrayList<>();
    optionDtos.add(optionDto1);
    when(optionService.getOptionsById(productId)).thenReturn(optionDtos);

    List<OptionDto> optionDtoByOptionService = optionService.getOptionsById(productId);

    assertThat(optionDtoByOptionService).isEqualTo(optionDtos);
  }

  @Test
  void deleteOptionExceptionTest() {
    Long id = 1L;
    doThrow(new EmptyResultDataAccessException(1))
      .when(optionService)
      .deleteOption(id);

    assertThrows(EmptyResultDataAccessException.class, () -> {
      optionService.deleteOption(id);
    });
  }

  @Test
  void updateOptionTest() {
    Long id = 1L;
    OptionDto optionDto1 = new OptionDto(1L, "옵션1", 2,
      new ProductDto(1L, "product1", 300, "fadsklf",
        new CategoryDto(1L, "교환권", "#6c95d1", "image_url", "교환권 카테고리")));
    OptionDto optionDto2 = new OptionDto(1L, "옵션232", 2,
      new ProductDto(1L, "product1", 300, "fadsklf",
        new CategoryDto(1L, "교환권", "#6c95d1", "image_url", "교환권 카테고리")));

    when(optionService.updateOption(id, optionDto2)).thenReturn(optionDto2);

    OptionDto updateOptionDtoByOptionService = optionService.updateOption(id, optionDto2);

    assertThat(updateOptionDtoByOptionService.getId()).isEqualTo(optionDto2.getId());
    assertThat(updateOptionDtoByOptionService.getName()).isEqualTo(optionDto2.getName());
    assertThat(updateOptionDtoByOptionService.getQuantity()).isEqualTo(optionDto2.getQuantity());
    assertThat(updateOptionDtoByOptionService.getProductDto()).isEqualTo(
      optionDto2.getProductDto());
  }
}
