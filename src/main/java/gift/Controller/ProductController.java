package gift.Controller;


import gift.DTO.PageResponseDTO;
import gift.DTO.ProductDTO;
import gift.DTO.ProductResponseDTO;
import gift.Model.Product;
import gift.Service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Product", description = "Product 관련 API")
@RestController
public class ProductController {

    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @Operation(
        summary = "모든 상품 가져오기",
        description = "등록된 모든 상품을 가져옴"
    )
    @ApiResponse(
        responseCode = "200",
        description = "모든 상품 가져오기 성공"
    )
    @Parameter(name = "pageable", description = "List에 담긴 Product객체를 개수에 맞춰서 page로 리턴")
    @GetMapping("/api/products")
    public ResponseEntity<Page<ProductResponseDTO>> getProducts(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size, @RequestParam(name = "sort", defaultValue = "name,asc") String[] sort, @RequestParam(name = "categoryId") Long categoryId) {
        Pageable pageable = PageRequest.of(page, size, productService.getSort(sort)); // 전달 받은 파라미터로 pageable 객체 생성
        //PageResponseDTO<ProductResponseDTO> response = productService.getResponse(pageable,categoryId);
        Page<ProductResponseDTO> response = productService.getResponse(pageable,categoryId);
        return ResponseEntity.ok().body(response);
    }

    @Operation(
        summary = "특정 상품 가져오기",
        description = "해당하는 ID의 상품을 가져옴"
    )
    @ApiResponse(
        responseCode = "200",
        description = "특정 상품 가져오기 성공"
    )
    @Parameter(name = "pageable", description = "List에 담긴 Product객체를 개수에 맞춰서 page로 리턴")
    @GetMapping("/api/products/{productId}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable(value = "productId") Long productId) {
        return ResponseEntity.ok().body(productService.getProductResponseDTOById(productId));
    }

    @Operation(
        summary = "상품 더하기",
        description = "상품을 product테이블에 저장"
    )
    @ApiResponse(
        responseCode = "200",
        description = "상품 더하기 성공"
    )
    @Parameter(name = "productDTO", description = "더할 상품 객체")
    @PostMapping("/api/products")
    public ResponseEntity<Product> createProduct(@Valid @ModelAttribute ProductDTO productDTO) {
        return ResponseEntity.ok().body(productService.addProduct(productDTO));
    }

    @Operation(
        summary = "상품 수정하기",
        description = "상품을 테이블에 업데이트"
    )
    @ApiResponse(
        responseCode = "200",
        description = "상품 업데이트 성공"
    )
    @Parameters({
        @Parameter(name = "productId", description = "수정할 상품 Id"),
        @Parameter(name = "newProductDTO", description = "수정할 상품 객체")
    })

    @PutMapping("/api/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable(value = "productId") Long productId, @Valid @ModelAttribute ProductDTO newProductDTO) {
        return ResponseEntity.ok().body(productService.updateProduct(newProductDTO));
    }

    @Operation(
        summary = "상품 삭제",
        description = "상품을 product테이블에서 삭제"
    )
    @ApiResponse(
        responseCode = "200",
        description = "상품 삭제 성공"
    )
    @Parameter(name = "productId", description = "삭제할 상품 객체 Id")
    @DeleteMapping("/api/products/{productId}")
    public ResponseEntity<Product> deleteProduct(@PathVariable(value = "productId") Long productId) {
        return ResponseEntity.ok().body(productService.deleteProduct(productId));
    }
}
