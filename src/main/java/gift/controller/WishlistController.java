package gift.controller;

import gift.dto.WishlistDTO;
import gift.model.User;
import gift.service.UserService;
import gift.service.WishlistService;
import gift.security.JwtTokenProvider;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishes")
public class WishlistController {

    private final WishlistService wishlistService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public WishlistController(WishlistService wishlistService, UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.wishlistService = wishlistService;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    public ResponseEntity<WishlistDTO> addWishlist(@RequestHeader("Authorization") String token, @RequestBody WishlistDTO wishlistDTO) {
        String email = jwtTokenProvider.getUsernameFromToken(token.substring(7)); // 'Bearer ' 제거
        User user = userService.loadOneUser(email);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        WishlistDTO createdWishlist = wishlistService.addWishlist(user, wishlistDTO);
        return new ResponseEntity<>(createdWishlist, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<WishlistDTO>> getWishlist(@RequestHeader("Authorization") String token,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam(defaultValue = "createdDate,desc") String[] sort) {
        String email = jwtTokenProvider.getUsernameFromToken(token.substring(7)); // 'Bearer ' 제거
        User user = userService.loadOneUser(email);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Page<WishlistDTO> wishes = wishlistService.getWishlist(user, page, size, sort);
        return new ResponseEntity<>(wishes, HttpStatus.OK);
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWishlist(@RequestHeader("Authorization") String token, @PathVariable("wishId") Long wishId) {
        String email = jwtTokenProvider.getUsernameFromToken(token.substring(7)); // 'Bearer ' 제거
        User user = userService.loadOneUser(email);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        wishlistService.deleteWishlist(user, wishId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}