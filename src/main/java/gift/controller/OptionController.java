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
}