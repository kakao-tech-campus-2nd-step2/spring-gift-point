package gift.controller.rest;

import gift.dto.option.OptionRequestDTO;
import gift.dto.option.OptionResponseDto;
import gift.dto.product.ProductRequestDto;
import gift.dto.product.ProductResponseDto;
import gift.dto.response.MessageResponseDTO;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Product 컨트롤러", description = "Product API입니다.")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // /api/products?page=1&size=3
    @Operation(summary = "모든 상품 조회", description = "모든 상품을 조회합니다.")
    @GetMapping()
    public ResponseEntity<List<ProductResponseDto>> getAllProducts(Pageable pageable) {
        List<ProductResponseDto> products = productService.findAll(pageable);
        return ResponseEntity.ok().body(products);
    }

    // category
    @Operation(summary = "카테고리 기준으로 모든 상품 조회", description = "카테고리 기준으로 모든 상품을 조회합니다.")
    @GetMapping("/categories")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategory(@RequestParam(name = "categoryId") Long categoryId,
                                                                          @RequestParam(name = "size", defaultValue = "20") int size, Pageable pageable) {
        List<ProductResponseDto> products = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok().body(products);
    }

    @Operation(summary = "상품 조회", description = "id로 상품을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable("id") Long id) {
        ProductResponseDto response = new ProductResponseDto(productService.findOne(id));
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "상품 생성", description = "상품을 생성합니다.")
    @PostMapping()
    public ResponseEntity<ProductResponseDto> postProduct(@RequestBody @Valid ProductRequestDto form, HttpSession session) {
        String email = (String) session.getAttribute("email");
        ProductResponseDto result = productService.save(form, email);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "상품 편집", description = "상품을 편집합니다.")
    @PutMapping(path = "/{id}")
    public ResponseEntity<ProductResponseDto> putProduct(@RequestBody @Valid ProductRequestDto form,
                                                         @PathVariable("id") Long id, HttpSession session) {
        String email = (String) session.getAttribute("email");
        ProductResponseDto result = productService.update(id, form, email);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<MessageResponseDTO> deleteProduct(@PathVariable("id") Long id, HttpSession session) {
        String email = (String) session.getAttribute("email");
        productService.delete(id, email);
        return ResponseEntity
                .ok()
                .body(makeMessageResponse("Product deleted successfully"));
    }

    // option
    @Operation(summary = "상품의 모든 옵션 조회", description = "상품의 모든 옵션을 조회합니다.")
    @GetMapping("/{id}/options")
    public ResponseEntity<List<OptionResponseDto>> getProductOptions(@PathVariable("id") Long id) {
        List<OptionResponseDto> result = productService.getOptions(id)
                .stream()
                .map(option -> new OptionResponseDto(option))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "상품에 옵션 저장", description = "상품에 옵션을 저장합니다.")
    @PostMapping("/{product_id}/options")
    public ResponseEntity<List<OptionResponseDto>> postProductOptions(@PathVariable("product_id") Long product_id, @RequestBody @Valid List<OptionRequestDTO> optionRequestDTOS, HttpSession session) {
        String email = (String) session.getAttribute("email");
        List<OptionResponseDto> result = productService.addProductOption(product_id, optionRequestDTOS, email)
                .stream()
                .map(option -> new OptionResponseDto(option))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "상품에 옵션 수정", description = "상품에 옵션을 수정합니다.")
    @PutMapping("/{product_id}/options/{option_id}")
    public ResponseEntity<MessageResponseDTO> putProductOptions(@PathVariable("product_id") Long product_id,
                                                                @PathVariable("option_id") Long option_id,
                                                                @RequestBody @Valid OptionRequestDTO optionRequestDTO, HttpSession session) {
        String email = (String) session.getAttribute("email");
        productService.editProductOption(product_id, option_id, optionRequestDTO, email);
        return ResponseEntity.ok().body(makeMessageResponse("Option edited successfully"));
    }

    @Operation(summary = "상품의 옵션 삭제", description = "상품의 옵션을 삭제합니다.")
    @DeleteMapping("/{product_id}/options/{option_id}")
    public ResponseEntity<MessageResponseDTO> deleteProductOptions(@PathVariable("product_id") Long product_id,
                                                                   @PathVariable("option_id") Long option_id,
                                                                   HttpSession session) {
        String email = (String) session.getAttribute("email");
        productService.deleteProductOption(product_id, option_id, email);
        return ResponseEntity
                .ok()
                .body(makeMessageResponse("Option deleted successfully"));
    }

    private MessageResponseDTO makeMessageResponse(String message) {
        return new MessageResponseDTO(message);
    }
}
