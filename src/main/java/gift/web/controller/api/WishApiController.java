package gift.web.controller.api;

import gift.authentication.annotation.LoginMember;
import gift.service.WishProductService;
import gift.web.dto.MemberDetails;
import gift.web.dto.request.wishproduct.CreateWishProductRequest;
import gift.web.dto.request.wishproduct.UpdateWishProductRequest;
import gift.web.dto.response.wishproduct.CreateWishProductResponse;
import gift.web.dto.response.wishproduct.ReadWishProductResponseByPromise;
import gift.web.dto.response.wishproduct.UpdateWishProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishApiController {

    private final WishProductService wishProductService;

    public WishApiController(WishProductService wishProductService) {
        this.wishProductService = wishProductService;
    }

    @GetMapping
    public ResponseEntity<Page<ReadWishProductResponseByPromise>> readWishProduct(@LoginMember MemberDetails memberDetails, @PageableDefault Pageable pageable) {
        Page<ReadWishProductResponseByPromise> response = wishProductService.readAllWishProducts(memberDetails.getId(), pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CreateWishProductResponse> createWishProduct(@Validated @RequestBody CreateWishProductRequest request, @LoginMember MemberDetails memberDetails) {
        CreateWishProductResponse response = wishProductService.createWishProduct(memberDetails.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{wishProductId}")
    public ResponseEntity<UpdateWishProductResponse> updateWishProduct(
        @PathVariable Long wishProductId,
        @Validated @RequestBody UpdateWishProductRequest request) {
        UpdateWishProductResponse response = wishProductService.updateWishProduct(
            wishProductId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{wishProductId}")
    public ResponseEntity<Void> deleteWishProduct(@PathVariable Long wishProductId) {
        wishProductService.deleteWishProduct(wishProductId);
        return ResponseEntity.noContent().build();
    }

}
