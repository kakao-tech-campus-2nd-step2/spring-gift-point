package gift.controller;

import gift.model.OptionDTO;
import gift.model.ProductDTO;
import gift.model.ProductPageDTO;
import gift.service.OptionService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
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
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final OptionService optionService;

    public ProductController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    @GetMapping
    @Operation(summary = "모든 상품을 조회합니다.", description = "쿼리 스트링으로 오프셋 페이지네이션을 지원합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ProductPageDTO.class))),
        @ApiResponse(responseCode = "400", description = "유효하지 않은 페이지 넘버나 사이즈를 입력했습니다.")
    })
    public ResponseEntity<?> getAllProduct(
        @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size) {
        ProductPageDTO productsPage = productService.getAllProduct(page, size);
        return ResponseEntity.ok().body(productsPage.products());
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 상품을 조회합니다.", description = "상품 ID로 특정 상품을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ProductDTO.class))),
        @ApiResponse(responseCode = "404", description = "잘못된 상품 ID를 입력했습니다.")
    })
    public ResponseEntity<ProductDTO> getProductById(
        @Parameter(description = "상품 ID", example = "1", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/{productId}/options")
    @Operation(summary = "특정 삼품의 옵션을 모두 조회합니다", description = "쿼리 스트링으로 페이지네이션을 지원합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = OptionDTO.class))),
        @ApiResponse(responseCode = "404", description = "잘못된 상품 ID를 입력했습니다.")
    })
    public ResponseEntity<?> getAllOptions(
        @Parameter(description = "상품 ID", example = "1", required = true)
        @PathVariable long productId, Pageable pageable) {
        List<OptionDTO> optionDTOList = productService.getOptionsByProductId(productId, pageable);
        return ResponseEntity.ok(optionDTOList);
    }

    @PostMapping
    @Operation(summary = "상품을 추가합니다.", description = "추가할 때 상품 정보는 상품 ID, 이름, 가격, 이미지 URL, 카테고리 ID 입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "상품 추가 성공", content = @Content(schema = @Schema(implementation = ProductDTO.class))),
        @ApiResponse(responseCode = "400", description = "유효하지 않은 입력값입니다.")
    })
    public ResponseEntity<ProductDTO> createProduct(
        @Parameter(description = "추가할 상품 정보", required = true)
        @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PostMapping("/{productId}/options")
    @Operation(summary = "상품에 옵션을 추가합니다.", description = "추가할 때 옵션 정보는 이름, 수량입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "옵션 추가 성공", content = @Content(schema = @Schema(implementation = ProductDTO.class))),
        @ApiResponse(responseCode = "400", description = "유효하지 않은 입력값입니다."),
        @ApiResponse(responseCode = "404", description = "잘못된 상품 ID를 입력했습니다.")
    })
    public ResponseEntity<?> createOption(
        @Parameter(description = "상품 ID", example = "1", required = true)
        @PathVariable long productId,
        @Parameter(description = "추가할 옵션 정보", required = true)
        @Valid @RequestBody OptionDTO optionDTO) {
        OptionDTO createdOption = optionService.createOption(productId, optionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOption);
    }

    @PutMapping("/{id}")
    @Operation(summary = "특정 상품을 업데이트합니다.", description = "상품 정보를 업데이트합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 업데이트 성공", content = @Content(schema = @Schema(implementation = ProductDTO.class))),
        @ApiResponse(responseCode = "400", description = "유효하지 않은 입력값입니다."),
        @ApiResponse(responseCode = "404", description = "잘못된 상품 ID를 입력했습니다.")
    })
    public ResponseEntity<ProductDTO> updateProduct(
        @Parameter(description = "업데이트할 상품 정보", required = true)
        @Valid @RequestBody ProductDTO updatedDTO) {
        ProductDTO updatedProduct = productService.updateProduct(updatedDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    @PutMapping("/{productId}/options/{optionId}")
    @Operation(summary = "특정 상품의 특정 옵션을 업데이트합니다.", description = "옵션 정보를 업데이트합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 업데이트 성공", content = @Content(schema = @Schema(implementation = ProductDTO.class))),
        @ApiResponse(responseCode = "400", description = "유효하지 않은 입력값입니다."),
        @ApiResponse(responseCode = "404", description = "잘못된 상품 ID 혹은 잘못된 옵션 ID 를 입력했습니다.")
    })
    public ResponseEntity<OptionDTO> updateOption(
        @Parameter(description = "상품 ID", example = "1", required = true)
        @PathVariable long productId,
        @Parameter(description = "옵션 ID", example = "1", required = true)
        @PathVariable long optionId,
        @Parameter(description = "업데이트할 옵션 정보", required = true)
        @Valid @RequestBody OptionDTO optionDTO) {
        OptionDTO updateOption = optionService.updateOption(productId, optionId, optionDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updateOption);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "특정 상품을 삭제합니다.", description = "상품 ID로 특정 상품을 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "삭제 완료"),
        @ApiResponse(responseCode = "404", description = "잘못된 ID 값을 입력했습니다.")
    })
    public ResponseEntity<Void> deleteProduct(
        @Parameter(description = "상품 ID", example = "1", required = true)
        @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{productId}/options/{optionId}")
    @Operation(summary = "특정 상품의 옵션을 삭제합니다.", description = "옵션 ID로 특정 옵션을 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "삭제 완료"),
        @ApiResponse(responseCode = "404", description = "잘못된 ID 값을 입력했습니다.")
    })
    public ResponseEntity<?> deleteOption(
        @Parameter(description = "옵션 ID", example = "1", required = true)
        @PathVariable long optionId) {
        optionService.deleteOption(optionId);
        return ResponseEntity.noContent().build();
    }
}
