package gift.product.controller;

import gift.option.domain.Option;
import gift.option.domain.OptionDTO;
import gift.option.service.OptionService;
import gift.product.domain.ProductDTO;
import gift.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@Tag(name = "Product")
public class ProductController {

    private final ProductService productService;
    private final OptionService optionService;

    public ProductController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    @GetMapping("")
    @Operation(summary = "product lsit, page형식으로 가져오기")
    public ResponseEntity<Page<ProductDTO>> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDTO> productPages = productService.getAllProducts(pageable);
        return new ResponseEntity<>(productPages, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "product id로 찾기")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        Optional<ProductDTO> productDTO = productService.getProductDTOById(id);
        return productDTO.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("")
    @Operation(summary = "product 생성")
    public ResponseEntity<Void> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        productService.createProduct(productDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "product 업데이트")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable("id") Long id, @Valid @RequestBody ProductDTO productDTO) {
        Optional<ProductDTO> existingProductDTO = productService.getProductDTOById(id);
        if (existingProductDTO.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.updateProduct(id, productDTO);
        return new ResponseEntity<>(productService.getProductDTOById(id).get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "id로 product 정보 삭제")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productService.getProductDTOById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("{id}/options")
    @Operation(summary = "optionList 가져오기")
    public ResponseEntity<List<OptionDTO>> getOptionList(@PathVariable Long id){
        return new ResponseEntity<>(optionService.findAllByProductId(id),HttpStatus.OK);
    }

    @PostMapping("/subtract")
    @Operation(summary = "option의 quantity 줄이기")
    public Option subtractQuantity(@RequestBody OptionDTO optionDTO, @RequestParam Long orderedQuantity) {
        return optionService.subtract(optionDTO, orderedQuantity);
    }
}
