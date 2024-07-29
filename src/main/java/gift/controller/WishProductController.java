package gift.controller;

import gift.dto.wishproduct.WishProductAddRequest;
import gift.dto.wishproduct.WishProductResponse;
import gift.dto.wishproduct.WishProductUpdateRequest;
import gift.service.WishProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/wishes")
@Tag(name = "WISH_PRODUCT")
public class WishProductController {

    private final WishProductService wishProductService;

    public WishProductController(WishProductService wishProductService) {
        this.wishProductService = wishProductService;
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addWishProduct(@Valid @RequestBody WishProductAddRequest wishProductAddRequest, @RequestAttribute("memberId") Long memberId) {
        var wishProduct = wishProductService.addWishProduct(wishProductAddRequest, memberId);
        return ResponseEntity.created(URI.create("/api/wishes/" + wishProduct.id())).build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateWishProduct(@PathVariable Long id, @Valid @RequestBody WishProductUpdateRequest wishProductUpdateRequest) {
        wishProductService.updateWishProduct(id, wishProductUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<WishProductResponse>> getWishProducts(@RequestAttribute("memberId") Long memberId,
                                                                     @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        var wishProducts = wishProductService.getWishProducts(memberId, pageable);
        return ResponseEntity.ok(wishProducts);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWishProduct(@PathVariable Long id) {
        wishProductService.deleteWishProduct(id);
        return ResponseEntity.noContent().build();
    }
}
