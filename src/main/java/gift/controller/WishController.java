package gift.controller;

import gift.domain.AuthToken;
import gift.dto.request.WishRequestDto;
import gift.dto.response.WishResponseDto;
import gift.service.TokenService;
import gift.service.WishService;
import gift.utils.PageableUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/wishes")
public class WishController {
    private final TokenService tokenService;
    private final WishService wishService;

    public WishController(TokenService tokenService, WishService wishService) {
        this.tokenService = tokenService;
        this.wishService = wishService;
    }

    @GetMapping
    public ResponseEntity<Page<WishResponseDto>> getWishProducts(HttpServletRequest request,
                                                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                                                 @RequestParam(name = "size", defaultValue = "10") int size,
                                                                 @RequestParam(name = "sort", defaultValue = "createdDate,desc") String sortBy
    ){
        AuthToken token = getAuthVO(request);
        Pageable pageable = PageableUtils.createPageable(page, size, sortBy);
        Page<WishResponseDto> findProducts = wishService.findWishesPaging(token.getEmail(), pageable);

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(findProducts);
    }

    @PostMapping
    public ResponseEntity<WishResponseDto> addWishProduct(HttpServletRequest request,
                                                          @RequestBody @Valid WishRequestDto wishRequestDto){
        AuthToken token = getAuthVO(request);
        WishResponseDto wishResponseDto = wishService.addWish(wishRequestDto.productId(), token.getEmail(), wishRequestDto.count());
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(wishResponseDto);
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<WishResponseDto> deleteLikesProduct(HttpServletRequest request,
                                                              @PathVariable("wishId") Long wishId){
        AuthToken token = getAuthVO(request);
        WishResponseDto wishResponseDto = wishService.deleteWish(wishId, token.getEmail());
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(wishResponseDto);
    }

    public AuthToken getAuthVO(HttpServletRequest request) {
        String key = request.getHeader("Authorization").substring(7);
        AuthToken token = tokenService.findToken(key);
        return token;
    }

}
