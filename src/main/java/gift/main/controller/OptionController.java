package gift.main.controller;

import gift.main.dto.OptionChangeQuantityRequest;
import gift.main.dto.OptionRequest;
import gift.main.dto.OptionResponse;
import gift.main.service.OptionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * *옵션리스트는 처음 프로덕트 등록시에만 사용한다.
 *
 * 1. 옵션 수정
 * 2. 옵션 추가
 * 3. 옵션 삭제 (최소 하나 이상 있어야한다.)
 *
 * */

@RestController
@RequestMapping("/admin/product")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    // 옵션리스트 반환
    @GetMapping("/{id}/options")
    public ResponseEntity<?> findAllOption(@PathVariable(value = "id") long productId) {
        List<OptionResponse> options = optionService.findAllOption(productId);
        return ResponseEntity.ok(options);
    }

    //특정 옵션 완전 삭제
    @DeleteMapping("/{productId}/option/{optionId}")
    public ResponseEntity<?> deleteOption(@PathVariable(value = "productId") long productId,
                                          @PathVariable(value = "optionId") long optionId) {
        optionService.deleteOption(productId, optionId);
        return ResponseEntity.ok("Option deleted successfully");
    }

    @PostMapping("/{productId}/option")
    public ResponseEntity<?> addOption(@PathVariable(value = "productId") long productId,
                                       @Valid @RequestBody OptionRequest optionRequest) {
        optionService.addOption(productId, optionRequest);
        return ResponseEntity.ok("Option added successfully");
    }

    @PutMapping("/{productId}/option/{optionId}")
    public ResponseEntity<?> updateOption(@PathVariable(value = "productId") long productId,
                                          @PathVariable(value = "optionId") long optionId,
                                          @Valid @RequestBody OptionRequest optionRequest) {
        optionService.updateOption(productId, optionId, optionRequest);
        return ResponseEntity.ok("Option updated successfully");
    }

    //옵션 수량 제거
    @PutMapping("/{productId}/option/{optionId}/quantity")
    public ResponseEntity<?> removeOptionQuantity(@PathVariable(value = "productId") long productId,
                                                  @PathVariable(value = "optionId") long optionId,
                                                  @Valid @RequestBody OptionChangeQuantityRequest optionChangeQuantityRequest) {
        optionService.removeOptionQuantity(optionId, optionChangeQuantityRequest);
        return ResponseEntity.ok("Option Quantity changed successfully");
    }
}
