package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import gift.dto.product.ShowProductDTO;
import gift.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class WishListController {
    private final WishListService wishListService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/wishlist/{product_id}")
    public String addWishList(@RequestHeader("Authorization") String token, @PathVariable int product_id) {
        wishListService.saveWishList(token, product_id);
        return "위시리스트 추가";
    }

    @GetMapping("/api/wishlist")
    public Page<ShowProductDTO> getWishList(@RequestHeader("Authorization") String token, @RequestParam(value = "page", defaultValue = "0") int pageNum) throws JsonProcessingException {
        Pageable pageable = PageRequest.of(pageNum, 2, Sort.by(Sort.Direction.ASC, "id"));
        return wishListService.getWishList(token, pageable);
    }

    @DeleteMapping("/api/wishlist/{product_id}")
    public String deleteWishList(@RequestHeader("Authorization") String token, @PathVariable int product_id) {
        wishListService.deleteWishList(token, product_id);
        return "위시리스트 삭제";
    }

}
