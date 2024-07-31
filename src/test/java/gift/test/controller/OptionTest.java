package gift.test.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import gift.controller.OptionController;
import gift.dto.OptionRequest;
import gift.dto.OptionResponse;
import gift.entity.Category;
import gift.entity.Product;
import gift.service.OptionService;

public class OptionTest {

	@Mock
	private OptionService optionService;

	@InjectMocks
	private OptionController optionController;

	@Mock
	private BindingResult bindingResult;

	private Category category;
	private Product product;
	private OptionRequest optionRequest;
	private OptionResponse optionResponse;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);

		category = new Category("교환권", "#6c95d1", "https://example.com/image.jpg", "");
		product = new Product("아이스 아메리카노 T", 4500, "https://example.com/image.jpg", category);
		product.setId(1L);
		
		optionRequest = new OptionRequest("01 [Best] 시어버터 핸드 & 시어 스틱 립 밤", 969);
		optionResponse = new OptionResponse(1L, "01 [Best] 시어버터 핸드 & 시어 스틱 립 밤", 969);
	}

	@Test
	public void testGetOptions() {
		when(optionService.getOptions(product.getId())).thenReturn(Collections.singletonList(optionResponse));
		
		ResponseEntity<List<OptionResponse>> response = optionController.getOptions(product.getId());
		
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(response.getBody().get(0).getName()).isEqualTo(optionResponse.getName());
	}
	
	@Test
	public void testAddOption() {
		doNothing().when(optionService).addOption(eq(product.getId()), any(OptionRequest.class), any(BindingResult.class));
	
		ResponseEntity<Void> response = optionController.addOption(product.getId(), optionRequest, bindingResult);
		
		assertThat(response.getStatusCodeValue()).isEqualTo(201);
	}
}
