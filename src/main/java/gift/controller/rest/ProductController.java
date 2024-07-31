package gift.controller.rest;

import gift.entity.option.Option;
import gift.entity.option.OptionDTO;
import gift.entity.product.Product;
import gift.entity.product.ProductDTO;
import gift.entity.product.ProductResponse;
import gift.entity.response.MessageResponseDTO;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Product 컨트롤러", description = "Product API입니다.")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // http://localhost:8080/api/products?page=1&size=3
    @Operation(summary = "모든 상품 조회", description = "모든 상품을 조회합니다.")
    @GetMapping()
    public List<Product> getAllProducts(Pageable pageable) {
        Page<Product> products = productService.findAll(pageable);
        return products.getContent();
    }

    // category
    @Operation(summary = "카테고리 기준으로 모든 상품 조회", description = "카테고리 기준으로 모든 상품을 조회합니다.")
    @GetMapping("/categories")
    public ResponseEntity<List<Product>> getProductsByCategory(@RequestParam(name = "categoryId") Long categoryId,
                                                               @RequestParam(name = "size", defaultValue = "20") int size, Pageable pageable) {
        Page<Product> products = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok().body(products.getContent());
    }

    @Operation(summary = "상품 조회", description = "id로 상품을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("id") Long id) {
        ProductResponse response = productService.findOne(id);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "상품 생성", description = "상품을 생성합니다.")
    @PostMapping()
    public ResponseEntity<Product> postProduct(@RequestBody @Valid ProductDTO form, HttpSession session) {
        String email = (String) session.getAttribute("email");
        Product result = productService.save(form, email);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "상품 편집", description = "상품을 편집합니다.")
    @PutMapping(path = "/{id}")
    public ResponseEntity<Product> putProduct(@RequestBody @Valid ProductDTO form,
                                              @PathVariable("id") Long id, HttpSession session) {
        String email = (String) session.getAttribute("email");
        Product result = productService.update(id, form, email);
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
    public ResponseEntity<List<Option>> getProductOptions(@PathVariable("id") Long id) {
        List<Option> result = productService.getOptions(id);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "상품에 옵션 저장", description = "상품에 옵션을 저장합니다.")
    @PostMapping("/{product_id}/options")
    public ResponseEntity<List<Option>> postProductOptions(@PathVariable("product_id") Long product_id, @RequestBody @Valid List<OptionDTO> optionDTOs, HttpSession session) {
        String email = (String) session.getAttribute("email");
        List<Option> res = productService.addProductOption(product_id, optionDTOs, email);
        return ResponseEntity.ok().body(res);
    }

    @Operation(summary = "상품에 옵션 수정", description = "상품에 옵션을 수정합니다.")
    @PutMapping("/{product_id}/options/{option_id}")
    public ResponseEntity<MessageResponseDTO> putProductOptions(@PathVariable("product_id") Long product_id,
                                                                @PathVariable("option_id") Long option_id,
                                                                @RequestBody @Valid OptionDTO optionDTO, HttpSession session) {
        String email = (String) session.getAttribute("email");
        productService.editProductOption(product_id, option_id, optionDTO, email);
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
