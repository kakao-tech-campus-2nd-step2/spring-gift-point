package gift.controller;

import gift.dto.PageRequestDTO;
import gift.dto.productDTOs.CustomProductPageDTO;
import gift.dto.productDTOs.ProductDTO;
import gift.service.ProductService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("api/products")
public class ProductApiController {
    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public CustomProductPageDTO getAllProducts(@RequestParam(defaultValue = "0") @Min(0) @Max(10000) int page) {
        PageRequestDTO pageRequestDTO = new PageRequestDTO(page, "id", "asc");
        return productService.getAllProductsByCustomPage(pageRequestDTO);
    }

    @GetMapping("/{productId}")
    @ResponseBody
    public ProductDTO getProductById(@PathVariable Long productId) {
        return productService.getProductDTOById(productId);
    }
}
