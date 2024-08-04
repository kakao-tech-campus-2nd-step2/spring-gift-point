package gift.controller;

import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.entity.Product;
import gift.service.WishlistService;
import gift.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/wishes")
public class WishlistController {

    private final WishlistService wishlistService;

    @Autowired
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping
    public ResponseEntity<?> addItem(HttpServletRequest request, @RequestBody WishRequest wishRequest) {
        Long memberId = (Long) request.getAttribute("memberId");

        try {
            wishlistService.addProduct(memberId, wishRequest);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getItems(HttpServletRequest request,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size) {
        Long memberId = (Long) request.getAttribute("memberId");

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<WishResponse> wishPage = wishlistService.getWishesByMemberId(memberId, pageRequest);

        Map<String, Object> response = new HashMap<>();
        response.put("total_page", wishPage.getTotalPages());
        response.put("content", wishPage.getContent());

        Map<String, Object> data = new HashMap<>();
        data.put("data", response);

        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        try {
            wishlistService.deleteItem(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
