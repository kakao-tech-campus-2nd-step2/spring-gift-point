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
    private final WishService wishService;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public WishController(WishService wishService, MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.wishService = wishService;
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<Wish> addWish(@RequestBody Wish wish, HttpServletRequest request) {
        String email = jwtUtil.getEmailFromRequest(request);
        if (email != null) {
            Optional<Member> memberOpt = memberRepository.findByEmail(email);
            if (memberOpt.isPresent()) {
                wish.setMember(memberOpt.get());
                Wish savedWish = wishService.save(wish);
                return ResponseEntity.ok(savedWish);
            }
        }
        return ResponseEntity.status(401).body(null);
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
