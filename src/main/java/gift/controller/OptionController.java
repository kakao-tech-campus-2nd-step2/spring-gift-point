package gift.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gift.dto.OptionRequest;
import gift.dto.OptionResponse;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "상품 옵션 관리", description = "상품 옵션 관련 API")
@RestController
@RequestMapping("/api/product/{productId}/options")
public class OptionController {
	
	private final OptionService optionService;
	
	public OptionController(OptionService optionService) {
		this.optionService = optionService;
	}
	
	@Operation(summary = "상품 옵션 조회", description = "상품에 대한 모든 옵션을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "옵션 리스트 반환 성공")
	@GetMapping
	public ResponseEntity<List<OptionResponse>> getOptions(@PathVariable("productId") Long productId){
		List<OptionResponse> options = optionService.getOptions(productId);
		return ResponseEntity.status(HttpStatus.OK).body(options);
	}
	
	@Operation(summary = "상품 옵션 추가", description = "상품에 새로운 옵션을 추가합니다.")
    @ApiResponse(responseCode = "201", description = "옵션 추가 성공")
	@PostMapping
	public ResponseEntity<Void> addOption(@PathVariable("productId") Long productId,
			@Valid @RequestBody OptionRequest request, BindingResult bindingResult){
		optionService.addOption(productId, request, bindingResult);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
