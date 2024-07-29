package gift.controller;


import gift.dto.product.ModifyProductDTO;
import gift.dto.product.SaveProductDTO;
import gift.dto.product.ShowProductDTO;
import gift.entity.Product;
import gift.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/api/products")
    public Page<ShowProductDTO> getProducts(@RequestParam(value = "page", defaultValue = "0") int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, 2, Sort.by(Sort.Direction.ASC, "id"));
        return productService.getAllProducts(pageable);
    }

    @PostMapping("/api/products")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String addProduct(@RequestBody SaveProductDTO product) {
        productService.saveProduct(product);
        return "product 추가";
    }

    @DeleteMapping("/api/products/{productId}")
    public String deleteProduct(@PathVariable int productId) {
        productService.deleteProduct(productId);
        return "product 삭제";
    }

    @PutMapping("/api/products/{productId}")
    public String updateProduct(@PathVariable int productId, @RequestBody ModifyProductDTO product) {
        productService.updateProduct(product);
        return "product 수정";
    }

    @GetMapping("/api/products/{id}")
    public ShowProductDTO getProduct(@PathVariable int id) {
        return productService.getProductByID(id);
    }
}

