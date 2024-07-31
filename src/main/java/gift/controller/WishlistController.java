package gift.controller;

import gift.dto.WishResponse;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.service.WishlistService;
import gift.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/wishes")
public class WishlistController {

    private final WishlistService wishlistService;
    private final JwtUtil jwtUtil;

    @Autowired
    public WishlistController(WishlistService wishlistService, JwtUtil jwtUtil) {
        this.wishlistService = wishlistService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItem(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> requestBody) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        Claims claims = jwtUtil.extractClaims(token);
        Long memberId = Long.parseLong(claims.getSubject());

        Long productId = Long.valueOf(requestBody.get("productId").toString());
        int productNumber = Integer.parseInt(requestBody.get("productNumber").toString());

        Member member = new Member();
        member.setId(memberId);
        Product product = new Product();
        product.setId(productId);

        Wish wish = new Wish();
        wish.setMember(member);
        wish.setProduct(product);
        wish.setProductNumber(productNumber);

        Wish addedWish = wishlistService.addProduct(wish);
        return ResponseEntity.ok(new WishResponse(addedWish.getId(), addedWish.getProduct().getId(), addedWish.getProduct().getName(), addedWish.getProductNumber()));
    }

    @GetMapping("/items")
    public ResponseEntity<?> getItems(@RequestHeader("Authorization") String token,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "id") String sortBy,
                                      @RequestParam(defaultValue = "asc") String direction) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        Claims claims = jwtUtil.extractClaims(token);
        Long memberId = Long.parseLong(claims.getSubject());

        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<WishResponse> wishPage = wishlistService.getWishesByMemberId(memberId, pageRequest);

        return ResponseEntity.ok(wishPage);
    }

    @GetMapping("/item-details/{productId}")
    public ResponseEntity<?> getProductDetails(@PathVariable Long productId) {
        try {
            Product product = wishlistService.getProductById(productId);
            return ResponseEntity.ok(product);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Product not found");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProductNumber(@PathVariable Long id, @RequestBody int productNumber) {
        try {
            wishlistService.updateProductNumber(id, productNumber);
            return ResponseEntity.ok("Successfully updated product quantity.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Product not found.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        try {
            wishlistService.deleteItem(id);
            return ResponseEntity.ok("Successfully deleted product.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Product not found.");
        }
    }
}
