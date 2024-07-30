package gift.controller;

import gift.exception.ErrorCode;
import gift.exception.customException.CustomArgumentNotValidException;
import gift.model.option.OptionDTO;
import gift.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Product Option Api")
@RestController
@RequestMapping("/api/products")
public class OptionController {

    private final ItemService itemService;

    public OptionController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Operation(summary = "상품 옵션 목록 조회", description = "상품 id를 통해 해당 상품의 옵션 목록을 조회합니다.")
    @GetMapping("/{id}/options")
    public ResponseEntity<List<OptionDTO>> getOptionList(
        @Parameter(description = "조회할 상품 id") @PathVariable("id") Long id) {
        List<OptionDTO> list = itemService.getOptionList(id);
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "특정 상품 옵션 추가")
    @PostMapping("/{id}/options")
    public ResponseEntity<Long> createOption(
        @Valid @RequestBody OptionDTO optionDTO,
        @Parameter(description = "옵션을 추가할 상품의 id") @PathVariable("id") Long itemId,
        BindingResult result)
        throws CustomArgumentNotValidException {
        if (result.hasErrors()) {
            throw new CustomArgumentNotValidException(result, ErrorCode.INVALID_INPUT);
        }
        return ResponseEntity.ok(itemService.insertOption(itemId, optionDTO));
    }

    @Operation(summary = "특정 옵션 수정")
    @PutMapping("/{id}/options/{optionId}")
    public ResponseEntity<Long> updateOption(@Valid @RequestBody OptionDTO optionDTO,
        @Parameter(description = "해당 옵션이 포함된 상품의 id(옵션의 id는 입력폼에서 전달)") @PathVariable("id") Long itemId,
        BindingResult result)
        throws CustomArgumentNotValidException {
        if (result.hasErrors()) {
            throw new CustomArgumentNotValidException(result, ErrorCode.INVALID_INPUT);
        }
        return ResponseEntity.ok(itemService.updateOption(itemId, optionDTO));
    }

    @Operation(summary = "특정 옵션 삭제")
    @DeleteMapping("/{item_id}/options/{optionId}")
    public ResponseEntity<Long> deleteOption(
        @Parameter(description = "상품 id") @PathVariable("item_id") Long itemId,
        @Parameter(description = "삭제될 옵션 id") @PathVariable("option_id") Long optionId) {
        itemService.deleteOption(itemId, optionId);
        return ResponseEntity.ok(optionId);
    }

}
