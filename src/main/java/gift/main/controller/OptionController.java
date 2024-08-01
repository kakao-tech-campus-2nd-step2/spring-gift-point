package gift.main.controller;

import gift.main.dto.OptionRequest;
import gift.main.dto.OptionResponse;
import gift.main.service.OptionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * *옵션리스트는 처음 프로덕트 등록시에만 사용한다.
 *
 * 1. 옵션 수정
 * 2. 옵션 추가
 * 3. 옵션 삭제 (최소 하나 이상 있어야한다.)
 *
 * */

@RestController
@RequestMapping("/api/options")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    //특정 제품의 옵션 목록 조회
    @GetMapping("/{product_id}")
    public ResponseEntity<?> findAllOption(@PathVariable(value = "product_id") long productId) {
        Map<String, Object> response = new HashMap<>();
        List<OptionResponse> options = optionService.findAllOption(productId);
        response.put("options", options);
        return ResponseEntity.ok(response);
    }

    //새로운 옵션 추가
    @PostMapping("/{product_id}")
    public ResponseEntity<?> addOption(@PathVariable(value = "product_id") long productId,
                                       @Valid @RequestBody OptionRequest optionRequest) {
        optionService.addOption(productId, optionRequest);
        return ResponseEntity.ok("Option added successfully");
    }

    //옵션 삭제
    @DeleteMapping("/{optionId}")
    public ResponseEntity<?> deleteOption(@PathVariable(value = "optionId") long optionId) {
        optionService.deleteOption(optionId);
        return ResponseEntity.ok("Option deleted successfully");
    }

    //옵션 업데이트
    @PutMapping("/{productId}/option/{optionId}")
    public ResponseEntity<?> updateOption(@PathVariable(value = "productId") long productId,
                                          @PathVariable(value = "optionId") long optionId,
                                          @Valid @RequestBody OptionRequest optionRequest) {
        optionService.updateOption(productId, optionId, optionRequest);
        return ResponseEntity.ok("Option updated successfully");
    }


}
