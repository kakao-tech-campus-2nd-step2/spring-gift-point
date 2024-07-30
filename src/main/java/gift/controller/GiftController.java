package gift.controller;

import gift.controller.dto.OptionResponse;
import gift.controller.dto.PaginationDTO;
import gift.controller.dto.ProductRequest;
import gift.controller.dto.ProductResponse;
import gift.domain.Product;
import gift.service.GiftService;
import gift.utils.PaginationUtils;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/products")
public class GiftController {

    private final GiftService giftService;

    public GiftController(GiftService giftService) {
        this.giftService = giftService;
    }


    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(@PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProductResponse> allProducts = giftService.getAllProduct(pageable);
        return ResponseEntity.ok(allProducts);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("productId") Long id) {
        ProductResponse productResponse = giftService.getProduct(id);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("/{id}/options")
    public ResponseEntity<List<OptionResponse>> getProductOption(@PathVariable("id") Long id){
        List<OptionResponse> option = giftService.getOption(id);
        return ResponseEntity.ok(option);
    }

}
