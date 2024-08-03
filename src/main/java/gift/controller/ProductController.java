package gift.controller;

import gift.LoginMember;
import gift.classes.RequestState.ProductPageRequestStateDTO;
import gift.classes.RequestState.ProductRequestStateDTO;
import gift.dto.MemberDto;
import gift.dto.ProductDto;
import gift.dto.ProductPageDto;
import gift.dto.RequestProductDto;
import gift.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "ProductController", description = "Product API")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

//    // 모든 제품 조회
//    @GetMapping
//    @Operation(summary = "모든 제품 조회", description = "저장된 모든 제품 정보를 조회하는 API")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "제품 목록 조회 성공"),
//        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
//    })

    // 특정 카테고리에 해당하는 모든 제품 조회(페이지네이션 적용)
    @GetMapping
    public ResponseEntity<ProductPageRequestStateDTO> getAllProducts(
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
        @RequestParam Long categoryId) {
        ProductPageDto productPageDto = productService.getAllProductsByCategoryId(page, size,
            categoryId);
        return ResponseEntity.ok().body(new ProductPageRequestStateDTO(
            HttpStatus.OK,
            "모든 상품을 상품을 조회했습니다.",
            productPageDto
        ));
    }

    // 특정 제품 조회
    @GetMapping("/{id}")
    @Operation(summary = "특정 제품 조회", description = "주어진 ID에 해당하는 제품 정보를 조회하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "제품 조회 성공"),
        @ApiResponse(responseCode = "404", description = "제품을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    public ResponseEntity<ProductRequestStateDTO> getProductById(@PathVariable Long id) {
        ProductDto productDto = productService.getProductById(id);
        return ResponseEntity.ok().body(new ProductRequestStateDTO(
            HttpStatus.OK,
            "상품을 조회했습니다.",
            productDto
        ));
    }

    // 제품 추가
    @PostMapping
    @Operation(summary = "제품 추가", description = "새로운 제품을 추가하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "제품 추가 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")

    })
    public ResponseEntity<ProductRequestStateDTO> addProduct(@LoginMember MemberDto memberDto,
        @Valid @RequestBody RequestProductDto requestProductDto) {
        ProductDto productDto = productService.addProduct(requestProductDto);
        return ResponseEntity.ok().body(new ProductRequestStateDTO(
            HttpStatus.OK,
            "상품이 등록되었습니다.",
            productDto
        ));
    }

    // 제품 수정
    @PutMapping("/{id}")
    @Operation(summary = "제품 수정", description = "주어진 ID에 해당하는 제품 정보를 수정하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "제품 수정 성공"),
        @ApiResponse(responseCode = "404", description = "제품을 찾을 수 없음"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    public ResponseEntity<ProductRequestStateDTO> updateProduct(@LoginMember MemberDto memberDto,
        @Valid @RequestBody RequestProductDto requestProductDto, @PathVariable Long id) {
        ProductDto productDto = productService.updateProduct(requestProductDto, id);
        return ResponseEntity.ok().body(new ProductRequestStateDTO(
            HttpStatus.OK,
            "상품이 수정되었습니다.",
            productDto
        ));
    }

    // 제품 삭제
    @DeleteMapping("/{id}")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "제품 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "제품을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    @Operation(summary = "제품 삭제", description = "주어진 ID에 해당하는 제품을 삭제하는 API")
    public ResponseEntity<ProductRequestStateDTO> deleteProduct(@LoginMember MemberDto memberDto,
        @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().body(new ProductRequestStateDTO(
            HttpStatus.OK,
            "상품이 수정되었습니다.",
            null
        ));
    }
}
