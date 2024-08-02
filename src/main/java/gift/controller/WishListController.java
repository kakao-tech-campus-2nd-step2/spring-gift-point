package gift.controller;

import gift.dto.wish.ResponseWishDTO;
import gift.dto.wish.WishPageDTO;
import gift.dto.wish.SaveWishlistDTO;
import gift.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class WishListController {
    private final WishListService wishListService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/wishes")
    public ResponseWishDTO addWishList(@RequestHeader("Authorization") String token, @RequestBody SaveWishlistDTO saveWishlistDTO) {
        return wishListService.saveWishList(token, saveWishlistDTO);
    }

    @GetMapping("/api/wishes")
    public WishPageDTO getWishList(@RequestHeader("Authorization") String token,
                                   @RequestParam(value = "page", defaultValue = "0") int pageNum,
                                   @RequestParam(value = "size", defaultValue = "10") int size,
                                   @RequestParam(value = "sort", defaultValue = "id,asc") String sortString) {

        List<String> sortStringList = Arrays.stream(sortString.split(",")).toList();
        String sortProperty = sortStringList.getFirst(), sortDirection = sortStringList.get(1);
        Pageable pageable;
        if (sortDirection.equals("asc"))
            pageable = PageRequest.of(pageNum, size, Sort.by(Sort.Direction.ASC, sortProperty));
        else
            pageable = PageRequest.of(pageNum, size, Sort.by(Sort.Direction.DESC, sortProperty));

        return wishListService.getWishList(token, pageable);
    }

    @DeleteMapping("/api/wishes/{wishId}")
    public ResponseWishDTO deleteWishList(@RequestHeader("Authorization") String token, @PathVariable int wishId) {
        return wishListService.deleteWishList(token, wishId);
    }

}
