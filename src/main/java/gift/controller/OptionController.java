package gift.controller;

import gift.annotation.LoginUser;
import gift.dto.OptionDTO;
import gift.dto.OrderRequestDTO;
import gift.dto.OrderResponseDTO;
import gift.entity.Option;
import gift.entity.Product;
import gift.service.OptionFacadeService;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/options")
@Tag(name = "Option(상품 옵션)", description = "상품 옵션 관련 API입니다.")
public class OptionController {

    private final OptionFacadeService optionService;

    public OptionController(OptionFacadeService optionService) {
        this.optionService = optionService;
    }


    @GetMapping()
    @Operation(summary = "ID로 Product 옵션 조회", description = "Product의 Id로 상품의 옵션을 가져옵니다.")
    public List<Option> getProductByIdWithOption(
            @Parameter(name = "productId", description = "Product Id", example = "1") @PathVariable("productId") long productId) {
        return optionService.getAllProductOption(productId);
    }

    @Operation(summary = "상품 Option 추가", description = "상품의 옵션을 추가합니다.")
    @PostMapping()
    public ResponseEntity<String> addOption(
            @PathVariable("productId") Long productId, @RequestBody OptionDTO optionDTO) {
        Product product = optionService.findProductById(productId);
        Option option = optionDTO.toEntity(product);
        optionService.addOption(option);

        return new ResponseEntity<>("Option 추가 완료", HttpStatus.CREATED);
    }

    @PutMapping("/{optionId}")
    @Operation(summary = "상품 Option 수정", description = "id값에 해당하는 상품의 옵션을 수정합니다.")
    public ResponseEntity<String> updateOption(@PathVariable("productId") Long productId,
                                               @PathVariable("optionId") Long optionId,
                                               @RequestBody OptionDTO optionDTO) {
        Product product = optionService.findProductById(productId);
        Option option = optionDTO.toEntity(product);
        optionService.updateOption(option, optionId);
        return new ResponseEntity<>("Option 수정 완료", HttpStatus.OK);
    }

    @DeleteMapping("/{optionId}")
    @Operation(summary = "상품 Option 삭제", description = "id값에 해당하는 상품의 옵션을 삭제합니다.")
    public ResponseEntity<String> deleteOption(@PathVariable("productId") Long productId,
                                               @PathVariable("optionId") Long optionId) {
        optionService.deleteOption(optionId);
        return new ResponseEntity<>("Option 삭제 완료", HttpStatus.NO_CONTENT);
    }



}
