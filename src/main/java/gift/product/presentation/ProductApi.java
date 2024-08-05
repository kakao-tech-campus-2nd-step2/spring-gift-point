package gift.product.presentation;

import gift.option.application.OptionServiceResponse;
import gift.option.presentation.request.OptionCreateRequest;
import gift.option.presentation.request.OptionUpdateRequest;
import gift.product.presentation.request.ProductCreateRequest;
import gift.product.presentation.response.ProductReadResponse;
import gift.product.presentation.request.ProductUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "상품 API")
public interface ProductApi {

    @Operation(summary = "상품 생성")
    @PostMapping
    ResponseEntity<?> create(
            @Parameter(description = "상품 생성 요청 정보", required = true)
            @Valid @RequestBody ProductCreateRequest request
    );

    @Operation(summary = "상품 목록 조회")
    @GetMapping
    ResponseEntity<Page<ProductReadResponse>> findAll(
            @Parameter(description = "페이징 정보", in = ParameterIn.QUERY) Pageable pageable,
            @Parameter(required = false, description = "카테고리 ID", in = ParameterIn.QUERY) Long categoryId
    );

    @Operation(summary = "상품 상세 조회")
    @GetMapping("/{id}")
    ResponseEntity<ProductReadResponse> findById(
            @Parameter(
                    description = "상품 ID",
                    in = ParameterIn.PATH,
                    required = true
            )
            @PathVariable("id") Long productId
    );

    @Operation(summary = "상품 수정")
    @PutMapping("/{id}")
    ResponseEntity<?> update(
            @Parameter(
                    description = "상품 ID",
                    in = ParameterIn.PATH,
                    required = true
            )
            @PathVariable("id") Long productId,
            @Parameter(description = "상품 수정 요청 정보", required = true)
            @Valid @RequestBody ProductUpdateRequest request
    );

    @Operation(summary = "상품 삭제")
    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(
            @Parameter(
                    description = "상품 ID",
                    in = ParameterIn.PATH,
                    required = true
            )
            @PathVariable("id") Long productId
    );

    @Operation(summary = "상품 옵션 조회")
    @GetMapping("/{id}/options")
    ResponseEntity<List<OptionServiceResponse>> findOptionsByProductId(
            @Parameter(
                    description = "상품 ID",
                    in = ParameterIn.PATH,
                    required = true
            )
            @PathVariable("id") Long productId
    );

    @Operation(summary = "상품 옵션 추가")
    @PostMapping("/{id}/options")
    ResponseEntity<?> addOption(
            @Parameter(
                    description = "상품 ID",
                    in = ParameterIn.PATH,
                    required = true
            )
            @PathVariable("id") Long productId,
            @Parameter(description = "상품 옵션 추가 요청 정보", required = true)
            @Valid @RequestBody OptionCreateRequest request
    );

    @Operation(summary = "상품 옵션 수정")
    @PutMapping("/{productId}/options/{optionId}")
    ResponseEntity<?> updateOption(
            @Parameter(
                    description = "상품 ID",
                    in = ParameterIn.PATH,
                    required = true
            )
            @PathVariable("productId") Long productId,
            @Parameter(
                    description = "상품 옵션 ID",
                    in = ParameterIn.PATH,
                    required = true
            )
            @PathVariable("optionId") Long optionId,
            @Parameter(description = "상품 옵션 수정 요청 정보", required = true)
            @Valid @RequestBody OptionUpdateRequest request
    );

    @Operation(summary = "상품 옵션 삭제")
    @DeleteMapping("/{productId}/options/{optionId}")
    ResponseEntity<?> deleteOption(
            @Parameter(
                    description = "상품 ID",
                    in = ParameterIn.PATH,
                    required = true
            )
            @PathVariable("productId") Long productId,
            @Parameter(
                    description = "상품 옵션 ID",
                    in = ParameterIn.PATH,
                    required = true
            )
            @PathVariable("optionId") Long optionId
    );
}
