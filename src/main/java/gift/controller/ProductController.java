package gift.controller;

import gift.dto.ProductDto;
import gift.dto.RequestProductDto;
import gift.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/products")
@Tag(name = "ProductController", description = "Product API")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 모든 제품 조회
    @GetMapping
    @Operation(summary = "모든 제품 조회", description = "저장된 모든 제품 정보를 조회하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "제품 목록 조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    // 특정 제품 조회
    @GetMapping("/{id}")
    @Operation(summary = "특정 제품 조회", description = "주어진 ID에 해당하는 제품 정보를 조회하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "제품 조회 성공"),
        @ApiResponse(responseCode = "404", description = "제품을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    public ProductDto getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // 제품 추가
    @PostMapping("/add")
    @Operation(summary = "제품 추가", description = "새로운 제품을 추가하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "제품 추가 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")

    })
    public ProductDto addProduct(@Valid @ModelAttribute RequestProductDto requestProductDto) {
        return productService.addProduct(requestProductDto);
    }

    // 제품 수정
    @PostMapping("/update/{id}")
    @Operation(summary = "제품 수정", description = "주어진 ID에 해당하는 제품 정보를 수정하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "제품 수정 성공"),
        @ApiResponse(responseCode = "404", description = "제품을 찾을 수 없음"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    public ProductDto updateProduct(@Valid @ModelAttribute RequestProductDto requestProductDto) {
        return productService.updateProduct(requestProductDto);
    }

    // 제품 삭제
    @GetMapping("/delete/{id}")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "제품 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "제품을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    @Operation(summary = "제품 삭제", description = "주어진 ID에 해당하는 제품을 삭제하는 API")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "{status: success}";
    }
}
