package gift.controller;

import gift.model.Member;
import gift.model.WishList;
import gift.repository.MemberRepository;
import gift.service.MemberService;
import gift.service.WishlistService;
import gift.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public WishlistController(WishlistService wishlistService, MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.wishlistService = wishlistService;
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItem(@RequestHeader("Authorization") String token, @RequestParam Long productId) {
        Claims claims = jwtUtil.extractClaims(token.replace("Bearer ", ""));
        Long memberId = Long.parseLong(claims.getSubject());
        boolean result = jwtUtil.isTokenValid(token.replace("Bearer ", ""), memberId);
        if (!result) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid!");
        }
        WishList addedItem = wishlistService.addProduct(memberId, productId);
        return ResponseEntity.ok(addedItem);
    }

    @GetMapping("/items")
    public String getItems(@RequestHeader("Authorization") String token, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, Model model) {
        Claims claims = jwtUtil.extractClaims(token.replace("Bearer ", ""));
        Long memberId = Long.parseLong(claims.getSubject());
        Page<WishList> wishlistPage = wishlistService.getProductsByMember(memberId, page, size);
        model.addAttribute("wishlistPage", wishlistPage);
        return "wishlist";
    }

    @PostMapping("/delete/{id}")
    public String deleteItem(@PathVariable("id") Long productId, RedirectAttributes redirectAttributes) {
        wishlistService.deleteById(productId);
        redirectAttributes.addFlashAttribute("message", "Product successfully deleted!");
        return "redirect:/wishlist/items";
    }
}
