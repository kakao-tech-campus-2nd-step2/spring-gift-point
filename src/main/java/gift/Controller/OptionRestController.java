package gift.Controller;

import gift.Model.DTO.OptionDTO;
import gift.Model.Entity.OptionEntity;
import gift.Service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "옵션 API", description = "옵션과 관련된 API")
@RestController
@RequestMapping("/api/products")
public class OptionRestController {
    private final OptionService optionService;

    public OptionRestController(OptionService optionService){
        this.optionService = optionService;
    }

    @PostMapping("{productId}/options")
    public ResponseEntity<?> create(@PathVariable Long productId, @RequestBody OptionDTO optionDTO){
        optionService.create(productId, optionDTO);
        return ResponseEntity.ok("성공");
    }

    @Operation(summary = "옵션 조회", description = "상품의 id를 받으며, 해당 상품의 모든 옵션을 조회한다.")
    @Parameter(name="productId", description = "상품의 id로, 해당 상품의 옵션을 조회하는데 사용된다.")
    @GetMapping("{productId}/options")
    public List<OptionDTO> read(@PathVariable Long productId){
        return optionService.read(productId);
    }

    @PutMapping("{productId}/options/{optionId}")
    public ResponseEntity<?> update(@PathVariable Long productId, @PathVariable Long optionId, @RequestBody OptionDTO optionDTO){
        optionService.update(productId, optionId, optionDTO);
        return ResponseEntity.ok("성공");
    }

    @DeleteMapping("{productId}/options/{optionId}")
    public ResponseEntity<?> delete(@PathVariable Long productId, @PathVariable Long optionId){
        optionService.delete(productId, optionId);
        return ResponseEntity.ok("성공");
    }
}
