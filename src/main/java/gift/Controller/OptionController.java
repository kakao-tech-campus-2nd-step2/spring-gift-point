package gift.Controller;

import gift.Model.Option;
import gift.Service.OptionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Option", description = "Option 관련 API")
@RestController
public class OptionController {
    private final OptionService optionService;

    public OptionController(OptionService optionService){
        this.optionService = optionService;
    }

    @Operation(
        summary = "상품의 옵션 가져오기",
        description = "등록된 모든 상품의 옵션 가져오기"
    )
    @ApiResponse(
        responseCode = "200",
        description = "옵션 가져오기 성공"
    )
    @Parameter(name = "productId", description = "가져올 상품의 ID")
    @GetMapping("/api/products/{productId}/options")
    public ResponseEntity<List<Option>> getOptions(@PathVariable(name = "productId") Long productId){
        return ResponseEntity.ok().body(optionService.getAllOptions(productId));
    }

    @Operation(
        summary = "상품의 옵션 추가",
        description = "해당되는 상품의 옵션 추가"
    )
    @ApiResponse(
        responseCode = "200",
        description = "옵션 추가 성공"
    )
    @Parameters({
        @Parameter(name = "option", description = "더할 옵션 객체"),
        @Parameter(name = "productId", description = "옵션에 해당하는 상품 ID")
    })
    @PostMapping("/api/products/{productId}/options")
    public ResponseEntity<Option> addOption(@RequestBody Option option, @PathVariable(name = "productId") Long productId){
        return ResponseEntity.ok().body(optionService.addOption(option, productId));
    }

    @Operation(
        summary = "옵션 수정하기",
        description = "등록된 상품의 옵션을 수정"
    )
    @ApiResponse(
        responseCode = "200",
        description = "옵션 수정 성공"
    )
    @Parameters({
        @Parameter(name = "option", description = "수정할 새로운 옵션"),
        @Parameter(name = "productId", description = "해당되는 상품 ID"),
        @Parameter(name = "optionId", description = "수정 되는 옵션 ID"),
    })
    @PutMapping("/api/products/{productId}/options/{optionId}")
    public ResponseEntity<Option> updateOption(@RequestBody Option option, @PathVariable(name = "productId") Long productId, @PathVariable(value = "optionId") Long optionId){
        return ResponseEntity.ok().body(optionService.updateOption(option,productId,optionId));
    }

    @Operation(
        summary = "옵션 삭제하기",
        description = "해당되는 옵션을 삭제"
    )
    @ApiResponse(
        responseCode = "200",
        description = "옵션 삭제 성공"
    )
    @Parameters({
        @Parameter(name = "productId", description = "삭제할 옵션의 상품 ID"),
        @Parameter(name = "optionId", description = "삭제할 옵션의 ID")
    })
    @DeleteMapping("/api/products/{productId}/options/{optionId}")
    public ResponseEntity<Option> deleteOption(@PathVariable(value = "productId") Long productId, @PathVariable(value = "optionId") Long optionId){
        return ResponseEntity.ok().body(optionService.deleteOption(optionId));
    }
}
