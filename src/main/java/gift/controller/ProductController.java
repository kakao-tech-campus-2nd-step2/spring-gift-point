package gift.controller;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.entity.Category;
import gift.entity.Product;
import gift.exception.CategoryException;
import gift.service.CategoryService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Product API")
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    //생성자 주입 권장
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @Operation(summary = "모든 상품 조회", description = "모든 상품을 조회합니다.",
        parameters = {
        @Parameter(name = "page", description = "페이지 번호"),
        @Parameter(name = "size", description = "한페이지 크기")
    })
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "상품 목록을 조회합니다.",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class))))
        })
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productService.findAll(pageable);
        List<ProductResponseDto> productList = productPage.stream()
            .map(product -> new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategory().getName()
            ))
            .collect(Collectors.toList());
        return new ResponseEntity<>(productList,HttpStatus.OK);
    }


    @Operation(summary = "상품 추가", description = "상품을 추가합니다.")
    @ApiResponses(
        value  = {
            @ApiResponse(
                responseCode = "200",
                description = "상품을 추가합니다.",
                content = @Content(schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "존재하지 않는 카테고리입니다."
            ),
            @ApiResponse(
                responseCode = "400",
                description = "이미존재하는 상품 입니다."
            )
        }
    )
    @PostMapping
    public ResponseEntity<String> addProducts(@Valid @RequestBody ProductRequestDto productRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        Category category = categoryService.findById(productRequestDto.getCategoryId())
            .orElseThrow(() -> new CategoryException("올바르지 않은 카테고리"));
        Product product = productRequestDto.toEntity(category);
        if (productService.addProduct(product)!=-1L) {
            return new ResponseEntity<>("상품 추가 완료", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("이미존재하는 상품 id", HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "상품 수정", description = "상품을 수정합니다.")
    @ApiResponses(
        value  = {
            @ApiResponse(
                responseCode = "200",
                description = "상품 수정성공.",
                content = @Content(schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "존재하지 않는 카테고리입니다."
            ),
            @ApiResponse(
                responseCode = "400",
                description = "존재하지 않는 상품 입니다."
            )
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<String> modifyProducts(@PathVariable("id") long id, @Valid @RequestBody  ProductRequestDto productRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        Category category = categoryService.findById(productRequestDto.getCategoryId())
            .orElseThrow(() -> new CategoryException("올바르지 않은 카테고리"));
        Product product = productRequestDto.toEntity(category);
        product.setId(id);
        if (productService.updateProduct(product)!=-1L) {
            return new ResponseEntity<>("상품 수정 완료", HttpStatus.OK);
        }
        return new ResponseEntity<>("존재하지 않는 상품 id", HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "상품 추가", description = "상품을 추가합니다.")
    @ApiResponses(
        value  = {
            @ApiResponse(
                responseCode = "200",
                description = "상품 삭제 완료.",
                content = @Content(schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "존재하지 않는 상품입니다."
            )
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProducts(@PathVariable("id") long id) {
        if (productService.deleteProduct(id)!=-1L) {
            return new ResponseEntity<>("상품삭제 완료", HttpStatus.OK);
        }
        return new ResponseEntity<>("없는 상품", HttpStatus.BAD_REQUEST);
    }





}