package gift.option.controller;

import gift.dto.HttpResult;
import gift.option.dto.OptionRequest;
import gift.option.dto.OptionResponse;
import gift.option.service.OptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products/{productId}/options")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping
    public ResponseEntity<OptionResponse> addOptions(
        @PathVariable(value = "productId") Long productId,
        @RequestBody OptionRequest optionRequest) {
        var option = optionService.createOption(productId, optionRequest);
        return ResponseEntity.ok(
            new OptionResponse(HttpResult.OK, "옵션 추가 성공", HttpStatus.OK, option));
    }

    @GetMapping
    public ResponseEntity<OptionResponse> getAllOptionsByProductId(
        @PathVariable(value = "productId") Long productId) {
        var optionList = optionService.findOptionsByProductId(productId);
//        List<Map<String, Object>> optionResponseList = optionList.stream()
//            .map(Option::getOptionResponseMap).toList();
        return ResponseEntity.ok(
            new OptionResponse(HttpResult.OK, "옵션 조회 성공", HttpStatus.OK, optionList));
    }

    @GetMapping("/{optionId}")
    public ResponseEntity<OptionResponse> getOptionById(@PathVariable("optionId") Long optionId,
        @PathVariable(value = "productId") Long productId) {
        var option = optionService.retrieveOption(productId,optionId);
        return ResponseEntity.ok(
            new OptionResponse(HttpResult.OK, "옵션 조회 성공", HttpStatus.OK, option));
    }

    @DeleteMapping("/{optionId}")
    public ResponseEntity<OptionResponse> deleteOptionById(
        @PathVariable(value = "productId") Long productId,
        @PathVariable(value = "optionId") Long optionId) {
        optionService.deleteOptions(productId, optionId);
        return ResponseEntity.ok(
            new OptionResponse(HttpResult.OK,
                "옵션 삭제 성공",
                HttpStatus.OK,
                null));
    }

    @PutMapping("{optionId}")
    public ResponseEntity<OptionResponse> updateOption(
        @PathVariable(value = "productId") Long productId,
        @PathVariable(value = "optionId") Long optionId, @RequestBody OptionRequest optionRequest) {
        var option = optionService.updateOption(productId, optionId, optionRequest);
        return ResponseEntity.ok(
            new OptionResponse(HttpResult.OK,
                "옵션 수정 성공",
                HttpStatus.OK
                , option));
    }
}
