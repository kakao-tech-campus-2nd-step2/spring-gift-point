package gift.controller;

import gift.model.dto.OptionDTO;
import gift.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}
