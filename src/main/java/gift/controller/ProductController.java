package gift.controller;

import gift.dto.OptionDto;
import gift.dto.ProductDto;
import gift.exception.ProductNotFoundException;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name="상품 API")
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Page<ProductDto> getProducts(Pageable pageable) {
        return productService.getProducts(pageable).map(product -> new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImgUrl(),
                product.getCategory().getId(),
                product.getOptions().stream()
                        .map(option -> new OptionDto(option.getId(), option.getName(), option.getQuantity()))
                        .collect(Collectors.toList())
        ));
    }

    @Operation(summary = "상품 리스트 리턴")
    @GetMapping
    public List<ProductDto> findAll() {
        return productService.findAll();
    }

    @Operation(summary = "특정 상품 조회")
    @GetMapping("/{productId}")
    public ProductDto getProductById(@PathVariable("id") Long id) {
        return productService.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다. ID: " + id));
    }

    @Operation(summary = "상품 추가", description = "프론트 연결 때는 쓰이지 않음")
    @PostMapping
    public Long addProduct(@RequestBody ProductDto productDto) {
        return productService.save(productDto);
    }

    @Operation(summary = "상품 수정", description = "프론트 연결 때는 쓰이지 않음")
    @PutMapping("/{productId}")
    public void updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        productService.update(id, productDto);
    }

    @Operation(summary = "상품 삭제", description = "프론트 연결 때는 쓰이지 않음")
    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable Long id) {
        productService.delete(id);
    }

    @Operation(summary = "상품 id로 옵션 조회")
    @GetMapping("/{productId}/options")
    public List<OptionDto> getProductOptions(@PathVariable Long productId) {
        return productService.getProductOptions(productId);
    }

    @Operation(summary = "해당 상품에 옵션 추가", description = "프론트 연결 때는 쓰이지 않음")
    @PostMapping("/{productId}/options")
    public void addOptionToProduct(@PathVariable Long productId, @RequestBody OptionDto optionDto) {
        productService.addOptionToProduct(productId, optionDto);
    }

    @Operation(summary = "해당 상품 수량 차감", description = "프론트 연결 때는 쓰이지 않음")
    @PostMapping("/{productId}/options/{optionId}/subtract")
    public void subtractOptionQuantity(@PathVariable Long productId, @PathVariable Long optionId, @RequestParam int quantity) {
        productService.subtractOptionQuantity(productId, optionId, quantity);
    }
}