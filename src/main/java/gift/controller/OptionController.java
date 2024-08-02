package gift.controller;

import gift.dto.OptionDTO;
import gift.service.OptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/options")
public class OptionController {
    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    /**
     * 제품 ID로 옵션 조회.
     *
     * @param productId 제품 ID
     * @return 옵션 목록
     */
    @GetMapping
    public ResponseEntity<List<OptionDTO>> getOptionsByProductId(@PathVariable Long productId) {
        List<OptionDTO> options = optionService.getOptionsByProductId(productId);
        return new ResponseEntity<>(options, HttpStatus.OK);
    }

    /**
     * 제품에 옵션 추가.
     *
     * @param productId 제품 ID
     * @param optionDTO 옵션 DTO
     * @return 생성된 옵션
     */
    @PostMapping
    public ResponseEntity<OptionDTO> addOptionToProduct(@PathVariable Long productId, @RequestBody OptionDTO optionDTO) {
        OptionDTO createdOption = optionService.addOptionToProduct(productId, optionDTO);
        return new ResponseEntity<>(createdOption, HttpStatus.CREATED);
    }

    /**
     * 제품 옵션 수정.
     *
     * @param productId 제품 ID
     * @param optionId 옵션 ID
     * @param optionDTO 수정할 옵션 DTO
     * @return 수정된 옵션 DTO
     */
    @PutMapping("/{optionId}")
    public ResponseEntity<OptionDTO> updateOption(@PathVariable Long productId, @PathVariable Long optionId, @RequestBody OptionDTO optionDTO) {
        OptionDTO updatedOption = optionService.updateOption(productId, optionId, optionDTO);
        return new ResponseEntity<>(updatedOption, HttpStatus.OK);
    }

    /**
     * 제품 옵션 삭제.
     *
     * @param productId 제품 ID
     * @param optionId 옵션 ID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long productId, @PathVariable Long optionId) {
        optionService.deleteOption(productId, optionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}