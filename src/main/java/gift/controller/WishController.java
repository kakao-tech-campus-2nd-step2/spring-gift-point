package gift.controller;

import gift.entity.Member;
import gift.entity.Wish;
import gift.repository.MemberRepository;
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
    @Autowired
    private WishService wishService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<Wish> addWish(@RequestBody Wish wish, HttpServletRequest request) {
        String email = getEmailFromRequest(request);
        if (email != null) {
            Optional<Member> memberOpt = memberRepository.findByEmail(email);
            if(memberOpt.isPresent()) {
                wish.setMember(memberOpt.get());
                wishService.save(wish);
                return ResponseEntity.ok(wish);
            }
        }
        return ResponseEntity.status(401).body(null);
    }

    @GetMapping
    public ResponseEntity<List<Wish>> getWishes(HttpServletRequest request) {
        String email = getEmailFromRequest(request);
        if (email != null) {
            Optional<Member> memberOpt = memberRepository.findByEmail(email);
            if(memberOpt.isPresent()) {
                return ResponseEntity.ok(wishService.findByMember(memberOpt.get()));
            }
        }
        return ResponseEntity.status(401).body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWish(@PathVariable Long id, HttpServletRequest request) {
        String email = getEmailFromRequest(request);
        if (email != null) {
            Optional<Member> memberOpt = memberRepository.findByEmail(email);
            if(memberOpt.isPresent()) {
                wishService.deleteById(id);
                return ResponseEntity.ok("Wish deleted successfully");
            }
        }
        return ResponseEntity.status(401).body(null);
    }

    // Extracting email from token using HttpServletRequest
    private String getEmailFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return jwtUtil.extractEmail(token);
        }
        return null;
    }
}
