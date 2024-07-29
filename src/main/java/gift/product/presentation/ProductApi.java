package gift.product.presentation;

import gift.option.application.OptionServiceResponse;
import gift.product.presentation.request.ProductCreateRequest;
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
    @PostMapping("")
    void create(
            @Parameter(description = "상품 생성 요청 정보", required = true)
            @Valid @RequestBody ProductCreateRequest request
    );

    @Operation(summary = "상품 목록 조회")
    @GetMapping("")
    ResponseEntity<Page<ProductControllerResponse>> findAll(
            @Parameter(description = "페이징 정보", in = ParameterIn.QUERY) Pageable pageable
    );

    @Operation(summary = "상품 상세 조회")
    @GetMapping("/{id}")
    ResponseEntity<ProductControllerResponse> findById(
            @Parameter(
                    description = "상품 ID",
                    in = ParameterIn.PATH,
                    required = true
            )
            @PathVariable("id") Long productId
    );

    @Operation(summary = "상품 수정")
    @PutMapping("/{id}")
    void update(
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
    void delete(
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
}
