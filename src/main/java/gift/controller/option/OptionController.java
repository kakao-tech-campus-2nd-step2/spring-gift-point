package gift.controller.option;

import gift.common.dto.PageResponse;
import gift.controller.option.dto.OptionRequest;
import gift.controller.option.dto.OptionResponse;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Option", description = "옵션 API")
@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/api/v1/products")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping("{productId}/options")
    @Operation(summary = "옵션 등록", description = "옵션을 등록합니다.")
    public ResponseEntity<Void> registerOption(
        @PathVariable("productId") Long productId,
        @Valid @RequestBody OptionRequest.Create request
    ) {
        Long id = optionService.register(productId, request);
        return ResponseEntity.created(URI.create("/api/v1/products/" + productId + "/options/" + id)).build();
    }

    @GetMapping("{productId}/options")
    @Operation(summary = "전체 옵션 등록", description = "전체 옵션을 조회합니다.")
    public ResponseEntity<PageResponse<OptionResponse>> getAllOption(
        @PathVariable("productId") Long productId,
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        PageResponse<OptionResponse> response = optionService.getAllProductOptions(productId,
            pageable);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("{productId}/options/{optionId}")
    @Operation(summary = "옵션 조회", description = "옵션을 조회합니다.")
    public ResponseEntity<OptionResponse> getOption(
        @PathVariable("productId") Long productId,
        @PathVariable("optionId") Long optionId
    ) {
        OptionResponse response = optionService.findOption(optionId);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{productId}/options/{optionId}")
    @Operation(summary = "옵션 수정", description = "옵션을 수정합니다.")
    public ResponseEntity<OptionResponse> updateOption(
        @PathVariable("productId") Long productId,
        @PathVariable("optionId") Long optionId,
        @Valid @RequestBody OptionRequest.Update request) {
        OptionResponse response = optionService.updateOption(productId, optionId, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{productId}/options/{optionId}")
    @Operation(summary = "옵션 삭제", description = "옵션을 삭제합니다.")
    public ResponseEntity deleteOption(
        @PathVariable("productId") Long productId,
        @PathVariable("optionId") Long optionId)
    {
        optionService.deleteOption(productId, optionId);
        return ResponseEntity.noContent().build();
    }
}
