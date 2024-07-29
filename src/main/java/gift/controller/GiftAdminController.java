package gift.controller;

import gift.controller.dto.ProductRequest;
import gift.controller.dto.ProductResponse;
import gift.service.GiftService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/products")
public class GiftAdminController {

    private final GiftService giftService;

    public GiftAdminController(GiftService giftService) {
        this.giftService = giftService;
    }


    @PostMapping
    public ResponseEntity<ProductResponse> postProduct(@Valid @RequestBody ProductRequest productRequest) {
        ProductResponse DTO = giftService.postProducts(productRequest);
        return new ResponseEntity<>(DTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
        @Valid @RequestBody ProductRequest productRequest) {
        ProductResponse DTO = giftService.putProducts(productRequest, id);
        return ResponseEntity.ok(DTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteProduct(@PathVariable Long id) {
        Long i = giftService.deleteProducts(id);
        return ResponseEntity.ok(i);
    }

}
