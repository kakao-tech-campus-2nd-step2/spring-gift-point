package gift.controller;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "product", description = "상품 API")
@RequestMapping("/product/jdbc")
@RestController()
public class productController {

    private final ProductService productService;

    public productController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("")
    @Operation(summary = "상품 응답 DTO 생성", description = "상품요청 DTO를 보내면, 상품응답 DTO를 만들어줍니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 생성 성공", content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<ProductResponseDto> createProductDto(@RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto productResponseDto = productService.createProductDto(
                productRequestDto.getName(),
                productRequestDto.getPrice(),
                productRequestDto.getUrl()
        );
        return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
    }

    @GetMapping("")
    @Operation(summary = "상품 목록 조회", description = "모든 상품 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공", content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<List<ProductResponseDto>> getAllProduct() {
        return new ResponseEntity<>(productService.getAllAndMakeProductResponseDto(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "상품 id로 상품 조회", description = "상품 id로 상품을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 조회 성공", content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<ProductResponseDto> getOneById(@Parameter(name = "id", description = "product 의 id", in = ParameterIn.PATH)
                                                         @PathVariable("id") Long id
    ) {
        return new ResponseEntity<>(productService.getProductResponseDtoById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "상품 정보 수정", description = "상품 id와 수정할 정보(ProductRequestDto)를 보내면 상품정보를 수정합니다.")
    public ResponseEntity<Void> update(@Parameter(name = "id", description = "product 의 id", in = ParameterIn.PATH)
                                       @PathVariable("id") Long id, @RequestBody ProductRequestDto productRequestDto
    ) {
        if (productService.update(id, productRequestDto.getName(), productRequestDto.getPrice(), productRequestDto.getUrl())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "상품 id로 상품 삭제", description = "상품 id로 상품을 삭제합니다.")
    public ResponseEntity<Void> delete(@Parameter(name = "id", description = "product 의 id", in = ParameterIn.PATH)
                                       @PathVariable("id") Long id
    ) {
        productService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{name}")
    @Operation(summary = "상품 이름으로 상품 조회", description = "상품 이름으로 상품을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 조회 성공", content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<ProductResponseDto> getOneByName(@Parameter(name = "name", description = "product 의 name", in = ParameterIn.PATH)
                                                           @PathVariable("name") String name) {
        return new ResponseEntity<>(productService.findProductByName(name), HttpStatus.OK);
    }

    @GetMapping("/products")
    @Operation(summary = "페이지네이션을 적용하여 모든 상품 조회", description = "모든 상품을 조회하되, 페이지네이션을 적용하여 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "페이지네이션으로 상품 목록 조회 성공", content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<Page<ProductResponseDto>> getProducts(Pageable pageable) {
        return new ResponseEntity<>(productService.getProducts(pageable), HttpStatus.OK);
    }
}
