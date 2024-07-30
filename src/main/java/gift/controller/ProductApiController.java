package gift.controller;

import gift.auth.CheckRole;
import gift.paging.PagingService;
import gift.request.ProductAddRequest;
import gift.response.ProductOptionsResponse;
import gift.response.ProductResponse;
import gift.request.ProductUpdateRequest;
import gift.exception.InputException;
import gift.model.Product;
import gift.service.OptionsService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductApiController {

    private final ProductService productService;
    private final OptionsService optionsService;
    private final PagingService pagingService;

    public ProductApiController(ProductService productService, OptionsService optionsService,
        PagingService pagingService) {
        this.productService = productService;
        this.optionsService = optionsService;
        this.pagingService = pagingService;
    }

    @CheckRole("ROLE_ADMIN")
    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> getAllProducts(@RequestParam(defaultValue = "1", name = "page") int page,
        @RequestParam(defaultValue = "10", name = "size") int size, @RequestParam(defaultValue = "id", name = "sort") String sort) {
        PageRequest pageRequest = pagingService.makeProductsPageRequest(page, size, sort);
        List<ProductResponse> dto = productService.getPagedAllProducts(pageRequest);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @CheckRole("ROLE_ADMIN")
    @PostMapping("/api/products")
    public ResponseEntity<Void> addProduct(@RequestBody @Valid ProductAddRequest dto,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputException(bindingResult.getAllErrors());
        }

        Product savedProduct = productService.addProduct(dto.name(), dto.price(), dto.imageUrl(),
            dto.categoryName());
        optionsService.addOption(dto.optionName(), dto.quantity(), savedProduct.getId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @CheckRole("ROLE_ADMIN")
    @PutMapping("/api/products/{productId}")
    public ResponseEntity<Void> updateProduct(@PathVariable("productId") Long id, @RequestBody @Valid ProductUpdateRequest dto,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputException(bindingResult.getAllErrors());
        }

        productService.updateProduct(id, dto.name(), dto.price(), dto.imageUrl(),
            dto.categoryName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @CheckRole("ROLE_ADMIN")
    @DeleteMapping("/api/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long id) {
        optionsService.deleteAllOptions(id);
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
