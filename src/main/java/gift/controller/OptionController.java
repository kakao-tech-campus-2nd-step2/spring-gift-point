package gift.controller;

import gift.dto.OptionRequestDTO;
import gift.dto.OptionResponseDTO;
import gift.service.OptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products/{productId}/options")
public class OptionController {

    @Autowired
    private OptionService optionService;

    // 특정 상품의 모든 옵션 조회
    @GetMapping
    public ResponseEntity<List<OptionResponseDTO>> getOptionsByProductId(@PathVariable Long productId) {
        List<OptionResponseDTO> options = optionService.getOptionsByProductId(productId);
        return ResponseEntity.ok(options);
    }

    // 새로운 옵션 생성
    @PostMapping
    public ResponseEntity<OptionResponseDTO> createOption(@PathVariable Long productId, @Valid @RequestBody OptionRequestDTO optionRequestDTO) {
        optionRequestDTO.setProductId(productId); // 상품 ID 설정
        OptionResponseDTO option = optionService.createOption(optionRequestDTO);
        return ResponseEntity.status(201).body(option);
    }

    // 옵션 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<OptionResponseDTO> updateOption(@PathVariable Long productId, @PathVariable Long id, @Valid @RequestBody OptionRequestDTO optionRequestDTO) {
        Optional<OptionResponseDTO> updatedOption = optionService.updateOption(id, optionRequestDTO);
        if (updatedOption.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedOption.get());
    }

    // 옵션 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long productId, @PathVariable Long id) {
        if (!optionService.deleteOption(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
