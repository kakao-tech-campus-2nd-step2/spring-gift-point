package gift.Controller;

import gift.DTO.ProductDto;
import gift.ResponseDto.ResponseResourceErrorDto;
import gift.Service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ProductController", description = "Product 관리 Controller")
@RestController
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }


  @Operation(summary = "모든 상품 가져오기", description = "데이터 베이스에 저장되어 있는 모든 상품을 가져온다.")
  @ApiResponse(responseCode = "200", description = "모든 상품 정보 가져오기 성공",
    content = @Content(mediaType = "application/json",
      schema = @Schema(implementation = Page.class),
      examples = @ExampleObject(
        value = """
              {
                  "content": [
                      {
                          "id": 1,
                          "name": "아이스 아메리카노",
                          "price": 4500,
                          "imageUrl": "https://example.com/image1.jpg",
                          "categoryDto": {
                              "id": 1,
                              "name": "카페인"
                          }
                      },
                      {
                          "id": 2,
                          "name": "아이스 라떼",
                          "price": 5000,
                          "imageUrl": "https://example.com/image2.jpg",
                          "categoryDto": {
                              "id": 1,
                              "name": "카페인"
                          }
                      }
                  ],
                  "pageable": {
                      "sort": {
                          "sorted": true,
                          "unsorted": false,
                          "empty": false
                      },
                      "pageNumber": 0,
                      "pageSize": 20,
                      "offset": 0,
                      "paged": true,
                      "unpaged": false
                  },
                  "last": true,
                  "totalPages": 1,
                  "totalElements": 2,
                  "size": 20,
                  "number": 0,
                  "sort": {
                      "sorted": true,
                      "unsorted": false,
                      "empty": false
                  },
                  "first": true,
                  "numberOfElements": 2,
                  "empty": false
              }
          """)
    ))
  @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
    content = @Content(schema = @Schema(implementation = ResponseResourceErrorDto.class)))
  @GetMapping
  public ResponseEntity<Page<ProductDto>> getAllProducts(Pageable pageable) {

    return ResponseEntity.ok(productService.getAllProducts(pageable));
  }

  @Operation(summary = "특정 상품 가져오기", description = "데이터 베이스에 저장되어 있는 특정 상품을 가져온다.")
  @ApiResponse(responseCode = "200", description = "특정 상품 정보 가져오기 성공",
    content = @Content(schema = @Schema(implementation = ProductDto.class)))
  @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
    content = @Content(schema = @Schema(implementation = ResponseResourceErrorDto.class)))
  @GetMapping("/{id}")
  public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
    ProductDto productDTO = productService.getProductById(id);

    return ResponseEntity.ok(productDTO);
  }

  @Operation(summary = "상품 등록하기", description = "데이터 베이스에 특정 상품을 저장한다.")
  @ApiResponse(responseCode = "201", description = "특정 상품 정보 저장 성공",
    content = @Content(schema = @Schema(implementation = ProductDto.class)))
  @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
    content = @Content(schema = @Schema(implementation = ResponseResourceErrorDto.class)))
  @PostMapping
  public ProductDto addProduct(@Valid @RequestBody ProductDto productDto) {

    return productService.addProduct(productDto);
  }

  @Operation(summary = "상품 업데이트 하기", description = "데이터 베이스에 있는 특정 상품을 정보를 업데이트한다.")
  @ApiResponse(responseCode = "200", description = "특정 상품 정보 업데이트 성공",
    content = @Content(schema = @Schema(implementation = ProductDto.class)))
  @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
    content = @Content(schema = @Schema(implementation = ResponseResourceErrorDto.class)))
  @PutMapping("/{id}")
  public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id,
    @RequestBody ProductDto updatedProductDto) {
    ProductDto existingProductDto = productService.updateProduct(id,
      updatedProductDto);

    return ResponseEntity.ok(updatedProductDto);

  }

  @Operation(summary = "상품 삭제하기", description = "데이터 베이스에 있는 특정 상품을 삭제 한다.")
  @ApiResponse(responseCode = "204", description = "특정 상품 삭제 성공",
    content = @Content(schema = @Schema(implementation = ProductDto.class)))
  @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근",
    content = @Content(schema = @Schema(implementation = ResponseResourceErrorDto.class)))
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    ProductDto existingProductDto = productService.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }
}
