package gift.controller.rest;

import gift.entity.MessageResponseDTO;
import gift.entity.Option;
import gift.entity.Product;
import gift.entity.ProductDTO;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 상품 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)))),
            @ApiResponse(responseCode = "401", description = "인가 기능이 확인되지 않은 접근",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @GetMapping()
    public List<Product> getAllProducts(Pageable pageable) {
        Page<Product> products = productService.findAll(pageable);
        return products.getContent();
    }

    @Operation(summary = "상품 조회", description = "id로 상품을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 조회 성공",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "401", description = "인가 기능이 확인되지 않은 접근",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "상품 조회 실패",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id) {
        Product product = productService.findById(id);
        return ResponseEntity.ok().body(product);
    }

    @Operation(summary = "상품 생성", description = "상품을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 생성 성공",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "401", description = "인가 기능이 확인되지 않은 접근",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @PostMapping()
    public ResponseEntity<Product> postProduct(@RequestBody @Valid ProductDTO form, HttpSession session) {
        String email = (String) session.getAttribute("email");
        Product result = productService.save(form, email);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "상품 편집", description = "상품을 편집합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 편집 성공",
                    content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "401", description = "인가 기능이 확인되지 않은 접근",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "대상 상품 조회 실패",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @PutMapping(path = "/{id}")
    public ResponseEntity<Product> putProduct(@RequestBody @Valid ProductDTO form,
                                              @PathVariable("id") Long id, HttpSession session) {
        String email = (String) session.getAttribute("email");
        Product result = productService.update(id, form, email);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 삭제 성공",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "인가 기능이 확인되지 않은 접근",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "대상 상품 조회 실패",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품의 옵션 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Option.class)))),
            @ApiResponse(responseCode = "401", description = "인가 기능이 확인되지 않은 접근",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @GetMapping("/{id}/options")
    public ResponseEntity<List<Option>> getProductOptions(@PathVariable("id") Long id) {
        List<Option> result = productService.getOptions(id);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "상품에 옵션 저장", description = "상품에 옵션을 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품에 옵션 저장 성공",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "인가 기능이 확인되지 않은 접근",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "대상 상품 혹은 옵션 조회 실패",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @PostMapping("/{product_id}/options/{option_id}")
    public ResponseEntity<MessageResponseDTO> postProductOptions(@PathVariable("product_id") Long product_id,
                                                                 @PathVariable("option_id") Long option_id,
                                                                 HttpSession session) {
        String email = (String) session.getAttribute("email");
        productService.addProductOption(product_id, option_id, email);
        return ResponseEntity
                .ok()
                .body(makeMessageResponse("Option added successfully"));
    }

    @Operation(summary = "상품의 옵션 삭제", description = "상품의 옵션을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품에 옵션 저장 성공",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "상품 옵션이 1개 이하일 때 옵션 삭제 불가",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "인가 기능이 확인되지 않은 접근",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
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
