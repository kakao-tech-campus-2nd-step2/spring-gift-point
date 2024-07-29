package gift.controller;

import gift.domain.AuthToken;
import gift.dto.request.WishCreateRequest;
import gift.dto.request.WishDeleteRequest;
import gift.dto.request.WishEditRequest;
import gift.dto.response.WishResponseDto;
import gift.service.TokenService;
import gift.service.WishService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/wishes")
public class WishController {
    private final TokenService tokenService;
    private final WishService wishService;

    public WishController(TokenService tokenService, WishService wishService) {
        this.tokenService = tokenService;
        this.wishService = wishService;
    }

    @ResponseBody
    @GetMapping
    public ResponseEntity<List<WishResponseDto>> getWishProducts(HttpServletRequest request,
                                                                 @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        AuthToken token = getAuthVO(request);

        List<WishResponseDto> findProducts = wishService.findWishesPaging(token.getEmail(), pageable);

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(findProducts);
    }
    @PostMapping
    public String addWishProduct(HttpServletRequest request,
                                 @RequestBody @Valid WishCreateRequest wishCreateRequest){
        AuthToken token = getAuthVO(request);

        wishService.addWish(wishCreateRequest.product_id(), token.getEmail(), wishCreateRequest.count());

        return "redirect:/wishes";
    }


    @PutMapping
    public String editWishProduct(HttpServletRequest request,
                                  @RequestBody @Valid WishEditRequest wishEditRequest){
        AuthToken token = getAuthVO(request);
        wishService.editWish(wishEditRequest.wish_id(), token.getEmail() , wishEditRequest.count());
        return "redirect:/wishes";
    }

    @DeleteMapping
    public String deleteLikesProduct(HttpServletRequest request,
                                     @RequestBody @Valid WishDeleteRequest wishDeleteRequest){
        AuthToken token = getAuthVO(request);
        wishService.deleteWish(wishDeleteRequest.wish_id(), token.getEmail());
        return "redirect:/wishes";
    }

    public AuthToken getAuthVO(HttpServletRequest request) {
        String key = request.getHeader("Authorization").substring(7);
        AuthToken token = tokenService.findToken(key);
        return token;
    }

}
