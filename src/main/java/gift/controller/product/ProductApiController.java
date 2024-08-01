package gift.controller.product;

import gift.dto.request.OptionRequestDto;
import gift.dto.request.ProductRequestDto;
import gift.dto.response.OptionResponseDto;
import gift.dto.response.ProductResponseDto;
import gift.service.OptionService;
import gift.service.ProductService;
import gift.utils.PageableUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    private final ProductService productService;
    private final OptionService optionService;

    public ProductApiController(ProductService productService,
                                OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> addProduct(@RequestBody @Valid ProductRequestDto productDto,
                                                         @RequestBody @Valid OptionRequestDto optionRequestDto){
        ProductResponseDto productResponseDto = productService.addProduct(productDto, optionRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(productResponseDto);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable("productId") Long productId){
        ProductResponseDto productResponseDto = productService.findProductById(productId);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(productResponseDto);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable("productId") Long productId,
                                                            @RequestBody @Valid ProductRequestDto productDto){
        ProductResponseDto productResponseDto = productService.updateProduct(productId, productDto);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(productResponseDto);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> delete(@PathVariable("productId") Long productId){
        ProductResponseDto productResponseDto = productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(productResponseDto);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                   @RequestParam(name = "size", defaultValue = "10") int size,
                                                                   @RequestParam(name = "sort", defaultValue = "name,asc") String sortBy,
                                                                   @RequestParam("categoryId") Long categoryId
    ){
        Pageable pageable = PageableUtils.createPageable(page, size, sortBy);
        Page<ProductResponseDto> productDtos = productService.findProductsUsingPaging(pageable, categoryId);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(productDtos);
    }

    @PostMapping("/{productId}/options")
    public ResponseEntity<OptionResponseDto> addProductOption(@PathVariable("productId") Long productId,
                                                              @RequestBody @Valid OptionRequestDto optionRequestDto){
        OptionResponseDto optionResponseDto = optionService.saveOption(optionRequestDto, productId);
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(optionResponseDto);
    }

    @PutMapping("/{productId}/options/{optionId}")
    public ResponseEntity<OptionResponseDto> editProductOption(@PathVariable("productId") Long productId,
                                                               @PathVariable("optionId") Long optionId,
                                                               @RequestBody @Valid OptionRequestDto optionRequestDto){
        OptionResponseDto optionResponseDto = optionService.updateOption(optionRequestDto, optionId);
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(optionResponseDto);
    }


    @DeleteMapping("/{productId}/options/{optionId}")
    public ResponseEntity<OptionResponseDto> deleteOption(@PathVariable("productId") Long productId,
                                                          @PathVariable("optionId") Long optionId){
        OptionResponseDto optionResponseDto = optionService.deleteOneOption(productId, optionId);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(optionResponseDto);
    }

    @GetMapping("/{productId}/options")
    public ResponseEntity<List<OptionResponseDto>> getProductOptions(@PathVariable("productId") Long productId){
        List<OptionResponseDto> optionDtos = optionService.findOptionsByProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(optionDtos);
    }

}
