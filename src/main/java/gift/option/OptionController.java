package gift.option;

import gift.option.model.OptionRequest;
import gift.option.model.OptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Option API", description = "Option 을 관리하는 API")
@RestController
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @Operation(summary = "특정 product 의 전체 option 조회", description = "id에 해당하는 product의 모든 option 조회")
    @Parameter(name = "id", description = "옵션을 조회할 product 의 id")
    @GetMapping("/api/products/{id}/options")
    public ResponseEntity<List<OptionResponse>> getAllOption(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(optionService.getOptions(productId));
    }

    @Operation(summary = "특정 product 에 option 추가", description = "id에 해당하는 product에 option을 추가")
    @Parameter(name = "id", description = "옵션을 추가할 product 의 id")
    @PostMapping("/api/products/{id}/options")
    public ResponseEntity<Void> addOption(@PathVariable("id") Long productId,
        @Valid @RequestBody OptionRequest.Create optionCreate) {
        return ResponseEntity.created(
            URI.create("/api/options/" + optionService.addOption(productId, optionCreate))).build();
    }

    @Operation(summary = "특정 option 수정", description = "id에 해당하는 option 을 수정합니다.")
    @Parameter(name = "optionId", description = "수정할 option 의 id")
    @PutMapping("/api/options/{optionId}")
    public ResponseEntity<Void> updateOption(@PathVariable("optionId") Long optionId,
        @Valid @RequestBody OptionRequest.Update optionUpdate) {
        optionService.updateOption(optionId, optionUpdate);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "특정 option 삭제", description = "id에 해당하는 option 을 삭제합니다.")
    @Parameter(name = "optionId", description = "삭제할 option 의 id")
    @DeleteMapping("/api/options/{optionId}")
    public ResponseEntity<Void> deleteOption(@PathVariable("optionId") Long optionId) {
        optionService.deleteOption(optionId);
        return ResponseEntity.ok().build();
    }
}
