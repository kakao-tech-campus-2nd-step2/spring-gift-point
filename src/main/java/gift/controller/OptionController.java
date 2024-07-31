package gift.controller;

import gift.dto.OptionDto;
import gift.model.product.Option;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/products/{productId}/options")
@RestController
@Tag(name = "Option Management", description = "Option Management API")
public class OptionController {
    private final OptionService optionService;

    public OptionController(OptionService optionService){
        this.optionService = optionService;
    }

    @GetMapping
    @Operation(summary = "상품의 전체 옵션 호출", description = "상품에 등록된 옵션을 불러올 때 사용하는 API")
    public ResponseEntity<List<OptionDto>> getAllOptionsById(@PathVariable Long productId) {
        List<Option> options = optionService.getAllOptionsById(productId);
        List<OptionDto> optionDtos = options.stream()
                .map(option -> new OptionDto(option.getName(), option.getAmount()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(optionDtos);
    }

    @PostMapping
    @Operation(summary = "새로운 옵션 추가", description = "새로운 옵션을 추가할 때 사용하는 API")
    public ResponseEntity<Void> addNewOption(@PathVariable Long productId, @RequestBody OptionDto optionDto) {
        optionService.addNewOption(productId, optionDto);
        return ResponseEntity.status(201).build();
    }
}
