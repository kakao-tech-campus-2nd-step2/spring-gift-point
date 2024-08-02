package gift.controller;

import gift.dto.OptionDto;
import gift.dto.ProductDto;
import gift.exception.ProductNotFoundException;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name="상품 API")
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "상품 목록 페이지네이션")
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductDto.class),
                    examples = @ExampleObject(
                            value = "{\"content\":[{\"id\":1,\"name\":\"Product 1\",\"price\":1000,\"imgUrl\":\"http://example.com/image1.jpg\",\"categoryId\":1,\"options\":[{\"id\":1,\"name\":\"Option 1\",\"quantity\":10}]}],\"pageable\":\"INSTANCE\",\"last\":false,\"totalPages\":1,\"totalElements\":1,\"size\":20,\"number\":0,\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"first\":true,\"numberOfElements\":1,\"empty\":false}"
                    )
            )
    )
    @GetMapping
    public Page<ProductDto> getProducts(
            @RequestParam(value = "categoryId", required = false) String categoryId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "name,asc") String sort
    ) {
        String[] sortParams = sort.split(",");
        Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);
        Sort sortObj = Sort.by(direction, sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        return productService.getProductsByCategory(categoryId, pageable);
    }


    @Operation(summary = "전체 상품 조회 : 프론트 연결 때는 쓰이지 않음")
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductDto.class),
                    examples = @ExampleObject(
                            value = "[{\"id\":1,\"name\":\"Product 1\",\"price\":1000,\"imgUrl\":\"http://example.com/image1.jpg\",\"categoryId\":1,\"options\":[{\"id\":1,\"name\":\"Option 1\",\"quantity\":10}]}]"
                    )
            )
    )
    @GetMapping("/all")
    public List<ProductDto> findAll() {
        return productService.findAll();
    }


    @Operation(summary = "특정 상품 조회")
    @ApiResponse(
            responseCode = "200",
            description = "성공적으로 특정 상품을 조회했습니다.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductDto.class),
                    examples = @ExampleObject(
                            value = "{\"id\":1,\"name\":\"Product 1\",\"price\":1000,\"imgUrl\":\"http://example.com/image1.jpg\",\"categoryId\":1,\"options\":[{\"id\":1,\"name\":\"Option 1\",\"quantity\":10}]}"
                    )
            )
    )
    @Parameter(name = "productId", description = "조회할 상품의 ID", example = "1")
    @GetMapping("/{productId}")
    public ProductDto getProductById(@PathVariable("productId") Long id) {
        return productService.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다. ID: " + id));
    }


    @Operation(summary = "상품 추가 : 프론트 연결 때는 쓰이지 않음")
    @ApiResponse(
            responseCode = "200",
            description = "성공적으로 상품을 추가했습니다.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Long.class),
                    examples = @ExampleObject(
                            value = "1"
                    )
            )
    )
    @PostMapping
    public Long addProduct(@RequestBody ProductDto productDto) {
        return productService.save(productDto);
    }


    @Operation(summary = "상품 수정 : 프론트 연결 때는 쓰이지 않음")
    @ApiResponse(
            responseCode = "200",
            description = "성공적으로 상품을 수정했습니다."
    )
    @Parameter(name = "productId", description = "수정할 상품의 ID", example = "1")
    @PutMapping("/{productId}")
    public void updateProduct(@PathVariable("productId") Long id, @RequestBody ProductDto productDto) {
        productService.update(id, productDto);
    }


    @Operation(summary = "상품 삭제 : 프론트 연결 때는 쓰이지 않음")
    @ApiResponse(
            responseCode = "200",
            description = "성공적으로 상품을 삭제했습니다."
    )
    @Parameter(name = "productId", description = "삭제할 상품의 ID", example = "1")
    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable("productId") Long id) {
        productService.delete(id);
    }


    @Operation(summary = "상품 id로 옵션 조회")
    @ApiResponse(
            responseCode = "200",
            description = "성공적으로 상품의 옵션을 조회했습니다.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = OptionDto.class),
                    examples = @ExampleObject(
                            value = "{\"id\":1,\"name\":\"Option 1\",\"quantity\":10,\"productId\":1}"
                    )
            )
    )
    @Parameter(name = "productId", description = "옵션을 조회할 상품의 ID", example = "1")
    @Parameter(name = "optionId", description = "조회할 옵션의 ID", example = "1")
    @GetMapping("/{productId}/options/{optionId}")
    public OptionDto getProductOption(
            @PathVariable("productId") Long productId,
            @PathVariable("optionId") Long optionId) {
        return productService.getProductOption(productId, optionId);
    }


    @Operation(summary = "해당 상품에 옵션 추가 : 프론트 연결 때는 쓰이지 않음")
    @ApiResponse(
            responseCode = "200",
            description = "성공적으로 옵션을 추가했습니다."
    )
    @Parameter(name = "productId", description = "옵션을 추가할 상품의 ID", example = "1")
    @PostMapping("/{productId}/options")
    public void addOptionToProduct(@PathVariable("productId") Long productId, @RequestBody OptionDto optionDto) {
        productService.addOptionToProduct(productId, optionDto);
    }


    @Operation(summary = "해당 상품 수량 차감 : 프론트 연결 때는 쓰이지 않음")
    @ApiResponse(
            responseCode = "200",
            description = "성공적으로 상품 옵션의 수량을 차감했습니다."
    )
    @Parameter(name = "productId", description = "수량을 차감할 상품의 ID", example = "1")
    @Parameter(name = "optionId", description = "수량을 차감할 옵션의 ID", example = "1")
    @Parameter(name = "quantity", description = "차감할 수량", example = "2")
    @PostMapping("/{productId}/options/{optionId}/subtract")
    public void subtractOptionQuantity(@PathVariable("productId") Long productId, @PathVariable("optionId") Long optionId, @RequestParam int quantity) {
        productService.subtractOptionQuantity(productId, optionId, quantity);
    }
}