package gift.controller;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.service.ProductService;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/product/jdbc")
@RestController()
public class productController {

    private final ProductService productService;

    public productController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("")
    public ResponseEntity<ProductResponseDto> createProductDto(@RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto productResponseDto = productService.createProductDto(
                productRequestDto.getName(),
                productRequestDto.getPrice(),
                productRequestDto.getUrl()
        );
        return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<ProductResponseDto>> getAll() {
        return new ResponseEntity<>(productService.getAllAndMakeProductResponseDto(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getOneById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(productService.getProductResponseDtoById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @RequestBody ProductRequestDto productRequestDto) {
        if (productService.update(id, productRequestDto.getName(), productRequestDto.getPrice(), productRequestDto.getUrl())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        productService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{name}")
    public ResponseEntity<ProductResponseDto> getOneByName(@PathVariable("name") String name) {
        return new ResponseEntity<>(productService.findProductByName(name), HttpStatus.OK);
    }

    @GetMapping("/products")
    public ResponseEntity<Page<ProductResponseDto>> getProducts(Pageable pageable) {
        return new ResponseEntity<>(productService.getProducts(pageable), HttpStatus.OK);
    }
}
