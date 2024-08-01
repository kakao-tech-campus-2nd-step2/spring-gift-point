package gift.controller;

import gift.dto.WishModificationResponse;
import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.exception.CustomException;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.service.WishService;
import gift.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/wishes")
@Tag(name = "Wish Management System", description = "Operations related to wish management")
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

    @GetMapping
    @Operation(summary = "Get wishes", description = "Fetches all wishes of the logged-in member", tags = { "Wish Management System" })
    public ResponseEntity<Page<WishResponse>> getWishes(HttpServletRequest request, Pageable pageable) {
        String email = jwtUtil.getEmailFromRequest(request);
        if (email == null) {
            throw new CustomException.InvalidCredentialsException("Invalid email");
        }
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new CustomException.EntityNotFoundException("Member not found"));
        Page<Wish> wishPage = wishService.findByMember(member, pageable);

        Page<WishResponse> responsePage = wishPage.map(WishResponse::new);

        return ResponseEntity.ok(responsePage);
    }

    @PostMapping
    @Operation(summary = "Add a wish", description = "Adds a new wish for a product", tags = { "Wish Management System" })
    public ResponseEntity<WishModificationResponse> addWish(
            @Parameter(description = "Wish object to be added", required = true)
            @RequestBody WishRequest wishRequest, HttpServletRequest request) {
        String email = jwtUtil.getEmailFromRequest(request);
        if (email == null) {
            throw new CustomException.InvalidCredentialsException("Invalid email");
        }
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new CustomException.EntityNotFoundException("Member not found"));
        Product product = productRepository.findById(wishRequest.getProductId()).orElseThrow(() -> new CustomException.EntityNotFoundException("Product not found"));
        Wish wish = wishService.modifyWish(member, product, wishRequest.getQuantity());
        WishModificationResponse wishResponse = new WishModificationResponse(wish.getId(), wish.getQuantity());

        return ResponseEntity.ok(wishResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a wish", description = "Deletes a wish by its ID", tags = { "Wish Management System" })
    public ResponseEntity<Void> deleteWish(
            @Parameter(description = "ID of the wish to be deleted", required = true)
            @PathVariable Long id, HttpServletRequest request) {
        String email = jwtUtil.getEmailFromRequest(request);
        if (email == null) {
            throw new CustomException.InvalidCredentialsException("Invalid email");
        }
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new CustomException.EntityNotFoundException("Member not found"));
        wishService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
