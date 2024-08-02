package gift.controller;


import gift.dto.product.ProductPageDTO;
import gift.dto.product.ResponseProductDTO;
import gift.dto.product.SaveProductDTO;
import gift.service.ProductService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/api/products")
    public ProductPageDTO getProductsInCategory(@RequestParam(value = "page", defaultValue = "0") int pageNum,
                                                @RequestParam(value = "size", defaultValue = "10") int size,
                                                @RequestParam(value = "sort", defaultValue = "name,asc") String sortString,
                                                @NotNull @RequestParam(value = "categoryId") int categoryId) {
        List<String> sortStringList = Arrays.stream(sortString.split(",")).toList();
        String sortProperty = sortStringList.getFirst(), sortDirection = sortStringList.get(1);
        Pageable pageable;
        if (sortDirection.equals("asc"))
            pageable = PageRequest.of(pageNum, size, Sort.by(Sort.Direction.ASC, sortProperty));
        else
            pageable = PageRequest.of(pageNum, size, Sort.by(Sort.Direction.DESC, sortProperty));
        return productService.getAllProductsInCategory(pageable, categoryId);
    }

    @PostMapping("/api/products")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseProductDTO addProduct(@RequestBody SaveProductDTO product) {
        return productService.saveProduct(product);

    }

    @DeleteMapping("/api/products/{id}")
    public ResponseProductDTO deleteProduct(@PathVariable int id) {
        return productService.deleteProduct(id);
    }

    @PutMapping("/api/products/{id}")
    public ResponseProductDTO updateProduct(@PathVariable int id, @RequestBody SaveProductDTO product) {
        return productService.updateProduct(id, product);
    }

    @GetMapping("/api/products/{id}")
    public ResponseProductDTO getProduct(@PathVariable int id) {
        return productService.getProductByID(id);
    }
}

