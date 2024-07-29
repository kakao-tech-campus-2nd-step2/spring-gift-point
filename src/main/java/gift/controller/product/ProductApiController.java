package gift.controller.product;

import gift.dto.response.OptionResponseDto;
import gift.service.CategoryService;
import gift.service.OptionService;
import gift.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/products")
public class ProductApiController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OptionService optionService;

    public ProductApiController(ProductService productService,
                                CategoryService categoryService,
                                OptionService optionService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.optionService = optionService;
    }


    @GetMapping("/{id}/options")
    public ResponseEntity<List<OptionResponseDto>> getProductOptions(@PathVariable("id") Long productId){
        List<OptionResponseDto> optionDtos = optionService.findOptionsByProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(optionDtos);
    }

}
