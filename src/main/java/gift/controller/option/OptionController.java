package gift.controller.option;

import gift.config.LoginAdmin;
import gift.config.LoginUser;
import gift.controller.auth.LoginResponse;
import gift.controller.category.CategoryResponse;
import gift.controller.response.ApiResponseBody;
import gift.controller.response.ApiResponseBuilder;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Option", description = "Option API")
@RequestMapping("/api/products")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/options")
    @Operation(summary = "get All Options", description = "모든 옵션 불러오기")
    public ResponseEntity<ApiResponseBody<Page<OptionResponse>>> getAllOptions(
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ApiResponseBuilder<Page<OptionResponse>>()
            .httpStatus(HttpStatus.OK)
            .data(optionService.findAll(pageable))
            .messages("모든 옵션 조회")
            .build();
    }

    @GetMapping("/{productId}/options")
    @Operation(summary = "get All Options By ProductId", description = "해당 옵션의 모든 옵션불러오기")
    public ResponseEntity<ApiResponseBody<List<OptionResponse>>> getAllOptionsByProductId(
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
        @PathVariable UUID productId) {
        return new ApiResponseBuilder<List<OptionResponse>>()
            .httpStatus(HttpStatus.OK)
            .data(optionService.findAllByProductId(productId))
            .messages("상품별 모든 옵션 조회")
            .build();
    }

    @GetMapping("/options/{optionId}")
    @Operation(summary = "get Option", description = "옵션 조회")
    public ResponseEntity<ApiResponseBody<OptionResponse>> getOption(@PathVariable UUID optionId) {
        return new ApiResponseBuilder<OptionResponse>()
            .httpStatus(HttpStatus.OK)
            .data(optionService.getOptionResponseById(optionId))
            .messages("옵션 조회")
            .build();
    }

    @PostMapping("{productId}/options")
    @Operation(summary = "create Option", description = "옵션 생성")
    public ResponseEntity<ApiResponseBody<OptionResponse>> createOption(@LoginUser LoginResponse member,
        @RequestBody OptionRequest option,
        @PathVariable UUID productId) {
        return new ApiResponseBuilder<OptionResponse>()
            .httpStatus(HttpStatus.OK)
            .data(optionService.save(productId, option))
            .messages("옵션 생성")
            .build();
    }

    @PutMapping("{productsId}/options/{optionId}")
    @Operation(summary = "modify Option", description = "옵션 수정")
    public ResponseEntity<ApiResponseBody<OptionResponse>> updateOption(@LoginUser LoginResponse member,
        @PathVariable UUID optionId, @RequestBody OptionRequest option) {
        return new ApiResponseBuilder<OptionResponse>()
            .httpStatus(HttpStatus.OK)
            .data(optionService.update(optionId, option))
            .messages("옵션 수정")
            .build();
    }

    @PutMapping("/options/{optionId}/buy/{quantity}")
    @Operation(summary = "substract option count", description = "옵션 구매(옵션 개수 1차감)")
    public ResponseEntity<ApiResponseBody<OptionResponse>> subtractOption(@LoginUser LoginResponse member,
        @PathVariable UUID optionId, @PathVariable Integer quantity) {
        return new ApiResponseBuilder<OptionResponse>()
            .httpStatus(HttpStatus.OK)
            .data(optionService.subtract(optionId, quantity))
            .messages("옵션 1개 차감")
            .build();
    }

    @DeleteMapping("/{productId}/options/{optionId}")
    @Operation(summary = "delete Option", description = "옵션 삭제")
    public ResponseEntity<ApiResponseBody<Void>> deleteOption(@LoginUser LoginResponse loginMember,
        @PathVariable UUID optionId) {
        optionService.delete(optionId);
        return new ApiResponseBuilder<Void>()
            .httpStatus(HttpStatus.OK)
            .data(null)
            .messages("옵션 삭제")
            .build();
    }
}