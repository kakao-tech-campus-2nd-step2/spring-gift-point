package gift.controller;

import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.service.WishService;
import gift.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/wishes")
public class WishController {
    private final WishService wishService;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public WishController(WishService wishService, MemberRepository memberRepository, ProductRepository productRepository, JwtUtil jwtUtil) {
        this.wishService = wishService;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<Wish> addWish(@RequestBody Wish wish, HttpServletRequest request) {
        String email = jwtUtil.getEmailFromRequest(request);
        if(email == null) {
            return ResponseEntity.status(401).body(null);
        }
        Optional<Member> memberOpt = memberRepository.findByEmail(email);
        if(memberOpt.isEmpty()) {
            return ResponseEntity.status(401).body(null);
        }
        Optional<Product> productOpt = productRepository.findById(wish.getProductId());
        if(productOpt.isEmpty()) {
            return ResponseEntity.status(401).body(null);
        }
        wish.setMember(memberOpt.get());
        wish.setProduct(productOpt.get());
        Wish savedWish = wishService.save(wish);
        return ResponseEntity.status(201).body(savedWish);
    }

    @GetMapping
    public ResponseEntity<List<Wish>> getWishes(HttpServletRequest request) {
        String email = jwtUtil.getEmailFromRequest(request);
        if (email != null) {
            Optional<Member> memberOpt = memberRepository.findByEmail(email);
            if (memberOpt.isPresent()) {
                List<Wish> wishes = wishService.findByMember(memberOpt.get());
                return ResponseEntity.ok(wishes);
            }
        }
        return ResponseEntity.status(401).body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWish(@PathVariable Long id, HttpServletRequest request) {
        String email = jwtUtil.getEmailFromRequest(request);
        if (email != null) {
            Optional<Member> memberOpt = memberRepository.findByEmail(email);
            if (memberOpt.isPresent()) {
                wishService.deleteById(id);
                return ResponseEntity.noContent().build(); // 204 No Content
            }
        }
        return ResponseEntity.status(401).build();
    }
}
