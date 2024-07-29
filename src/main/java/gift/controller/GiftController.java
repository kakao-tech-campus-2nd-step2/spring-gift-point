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
    public ResponseEntity<Page<ProductResponse>> getAllProducts(@ModelAttribute PaginationDTO paginationDTO,
        @RequestHeader("Authorization") String token) {
        Pageable pageable = PaginationUtils.createPageable(paginationDTO, "product");
        Page<ProductResponse> allProducts = giftService.getAllProduct(pageable);
        return ResponseEntity.ok(allProducts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse productResponse = giftService.getProduct(id);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("/{id}/options")
    public ResponseEntity<List<OptionResponse>> getProductOption(@PathVariable Long id){
        List<OptionResponse> option = giftService.getOption(id);
        return ResponseEntity.ok(option);
    }

}
