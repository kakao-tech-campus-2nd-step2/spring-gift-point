package gift.controller;

import gift.component.LoginMember;
import gift.dto.ApiResponse;
import gift.dto.WishDTO;
import gift.model.Member;
import gift.model.Product;
import gift.model.Wish;
import gift.service.ProductService;
import gift.service.WishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;
    private final ProductService productService;

    @Autowired
    public WishController(WishService wishService, ProductService productService) {
        this.wishService = wishService;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<WishDTO>>> getWishes(
        @LoginMember Member member,
        @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Wish> wishes = wishService.getWishesByMemberId(member.getId(), pageable);
        Page<WishDTO> wishDTOs = wishes.map(wish -> new WishDTO(wish.getId(), wish.getMember().getId(), wish.getProduct().getId()));
        ApiResponse<Page<WishDTO>> response = new ApiResponse<>(true, "Wishes retrieved successfully.", wishDTOs, null);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<WishDTO>> addWish(@RequestBody Map<String, Long> wishRequest, @LoginMember Member member) {
        try {
            Long productId = wishRequest.get("productId");
            Product product = productService.getProductById(productId);
            Wish wish = new Wish();
            wish.setMember(member);
            wish.setProduct(product);
            Wish savedWish = wishService.addWish(wish);
            WishDTO wishDTO = new WishDTO(savedWish.getId(), savedWish.getMember().getId(), savedWish.getProduct().getId());
            ApiResponse<WishDTO> response = new ApiResponse<>(true, "Wish added successfully.", wishDTO, null);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception ex) {
            ApiResponse<WishDTO> response = new ApiResponse<>(false, "Failed to add wish: " + ex.getMessage(), null, "500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> removeWish(@PathVariable Long id, @LoginMember Member member) {
        try {
            boolean removed = wishService.removeWish(id, member.getId());
            if (removed) {
                ApiResponse<Void> response = new ApiResponse<>(true, "Wish removed successfully.", null, null);
                return ResponseEntity.ok(response);
            }
            ApiResponse<Void> response = new ApiResponse<>(false, "Wish not found or not owned by member.", null, "404");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception ex) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Failed to remove wish: " + ex.getMessage(), null, "500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
