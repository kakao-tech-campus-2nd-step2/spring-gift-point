package gift.controller;

import gift.dto.WishPatchDto;
import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.service.JwtProvider;
import gift.service.WishService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishes")
public class WishController {
    private final JwtProvider jwtProvider;
    private final WishService wishService;
    public WishController(JwtProvider jwtProvider, WishService wishService) {
        this.jwtProvider = jwtProvider;
        this.wishService = wishService;
    }

    @PostMapping
    public ResponseEntity<Void> addWish(@RequestHeader("Authorization") String fullToken, @RequestBody WishRequestDto wishRequestDto){
        String userEmail = jwtProvider.getMemberEmail(fullToken.substring(7));
        wishService.save(userEmail,wishRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Page<WishResponseDto>> findWishesByMemberId(
        @RequestHeader("Authorization") String fullToken,
        @PageableDefault(sort = "createdAt", direction = Sort.Direction.ASC, size = 10) Pageable pageable) {
        String userEmail = jwtProvider.getMemberEmail(fullToken.substring(7));
        return ResponseEntity.status(HttpStatus.OK).body(wishService.findByEmailPage(userEmail,pageable));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteWish(@RequestHeader("Authorization") String fullToken, @PathVariable Long productId){
        String userEmail = jwtProvider.getMemberEmail(fullToken.substring(7));
        wishService.deleteByUser(userEmail);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<Void> updateWish(@RequestHeader("Authorization") String fullToken, @PathVariable Long productId,@RequestBody
        WishPatchDto wishPatchDto){
        String userEmail = jwtProvider.getMemberEmail(fullToken.substring(7));
        wishService.updateWish(userEmail,productId,wishPatchDto.getQuantity());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
