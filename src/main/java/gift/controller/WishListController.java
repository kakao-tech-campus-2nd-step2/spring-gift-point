package gift.controller;

import gift.dto.WishListRequestDTO;
import gift.dto.WishListResponseDTO;
import gift.enums.SortDirection;
import gift.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishes")
public class WishListController {

    private final WishListService wishListService;

    @Autowired
    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    // 사용자의 위시 리스트를 조회
    @GetMapping
    public ResponseEntity<Page<WishListResponseDTO>> getWishlist(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdDate") String sortBy,
        @RequestParam(defaultValue = "DESC") SortDirection direction,
        @RequestHeader("Authorization") String token) {

        Sort sort = Sort.by(direction == SortDirection.DESC ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<WishListResponseDTO> wishlist = wishListService.getWishlist(token, pageRequest);
        return ResponseEntity.ok(wishlist);
    }

    // 위시 리스트에 상품을 추가
    @PostMapping
    public ResponseEntity<Void> addProductToWishlist(
        @RequestBody WishListRequestDTO wishListRequestDTO,
        @RequestHeader("Authorization") String token) {
        wishListService.addProductToWishlist(token, wishListRequestDTO);
        return ResponseEntity.status(201).build();
    }

    // 위시 리스트에서 상품을 삭제
    @DeleteMapping("/{wishListId}")
    public ResponseEntity<Void> removeProductFromWishlist(
        @PathVariable Long wishListId,
        @RequestHeader("Authorization") String token) {
        wishListService.removeProductFromWishlist(token, wishListId);
        return ResponseEntity.noContent().build();
    }
}
