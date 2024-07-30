package gift.product.controller;

import gift.product.option.domain.Option;
import gift.product.option.domain.OptionDTO;
import gift.product.option.service.OptionService;
import gift.product.domain.ProductDTO;
import gift.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@Tag(name = "상품 API")
public class ProductController {

    private final ProductService productService;
    private final OptionService optionService;

    public ProductController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    @GetMapping("")
    @Operation(summary = "상품목록조회")
    public ResponseEntity<Page<ProductDTO>> getAllProductsWishCategoryId(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "name,asc") String sort,
        @RequestParam(required = false) Long categoryId){

        String[] sortParams = sort.split(",");
        Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));

        Page<ProductDTO> products;

        if(categoryId != null){
            products = productService.getAllProductsByCategoryId(categoryId, pageable);
        }else{
            products = productService.getAllProducts(pageable);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "product id로 찾기")
    public ResponseEntity<ProductDTO> findById(@PathVariable("productId") Long id) {
        Optional<ProductDTO> productDTO = productService.getProductDTOById(id);
        return productDTO.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("")
    @Operation(summary = "상품 생성")
    public ResponseEntity<Void> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        productService.createProduct(productDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "상품 수정")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable("productId") Long id, @Valid @RequestBody ProductDTO productDTO) {
        Optional<ProductDTO> existingProductDTO = productService.getProductDTOById(id);
        if (existingProductDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try{
            return new ResponseEntity<>(productService.updateProduct(id, productDTO), HttpStatus.OK);
        }catch(EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "상품 삭제")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long id) {
        if (productService.getProductDTOById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("{productId}/options")
    @Operation(summary = "상품옵션 목록 조회")
    public ResponseEntity<List<OptionDTO>> getOptionList(@PathVariable Long productId){
        return new ResponseEntity<>(optionService.findAllByProductId(productId),HttpStatus.OK);
    }

    @PostMapping("{productId}/options/{optionId}")
    @Operation(summary = "상품 옵션 추가")
    public ResponseEntity<OptionDTO> addOption(@PathVariable Long productId,
        @PathVariable Long optionId,
        @RequestBody OptionDTO optionDTO){
        try{
            optionDTO = optionService.addOption(productId, optionId, optionDTO);
        }catch(IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(optionDTO);
    }

    @PutMapping("{productId}/options/{optionId}")
    @Operation(summary = "상품 옵션 수정")
    public ResponseEntity<OptionDTO> updateOption(@PathVariable Long productId,
        @PathVariable Long optionId,
        @RequestBody OptionDTO optionDTO){
        try{
            optionDTO = optionService.updateOption(productId, optionId, optionDTO);
            return new ResponseEntity<>(optionDTO, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{productId}/options/{optionId}")
    @Operation(summary = "상품 옵션 삭제")
    public ResponseEntity<Void> deleteOption(@PathVariable Long productId,
        @PathVariable Long optionId){
        optionService.deleteOption(productId, optionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/subtract")
    @Operation(summary = "option의 quantity 줄이기")
    public Option subtractQuantity(@RequestBody OptionDTO optionDTO, @RequestParam Long orderedQuantity) {
        return optionService.subtract(optionDTO, orderedQuantity);
    }
}
