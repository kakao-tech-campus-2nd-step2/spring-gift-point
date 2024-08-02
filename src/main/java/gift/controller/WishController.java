package gift.controller;

import gift.dto.request.LoginMemberDTO;
import gift.dto.request.TokenLoginRequestDTO;
import gift.dto.request.WishRequestDTO;
import gift.dto.response.PagingWishResponseDTO;
import gift.dto.response.WishResponseDTO;
import gift.service.LoginMember;
import gift.service.WishService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/wishes")
public class WishController {
    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }


    @PostMapping("")
    public ResponseEntity<String> addWish(@RequestBody WishRequestDTO wishRequestDTO, @LoginMember LoginMemberDTO loginMemberDTO){
        wishService.addWish(wishRequestDTO, loginMemberDTO);
        return ResponseEntity.ok("Wish added");
    }


    @DeleteMapping("/{wishId}")
    public ResponseEntity<String> deleteWish(@PathVariable("wishId") Long wishId,
                                             @LoginMember LoginMemberDTO loginMemberDTO) {
        System.out.println("delete");
        wishService.removeWish(wishId, loginMemberDTO);
        return ResponseEntity.ok("Wish deleted");
    }

    @GetMapping("")
    public ResponseEntity<PagingWishResponseDTO> getWishes(@LoginMember LoginMemberDTO loginMemberDTO,
                                                           @RequestParam(name="page", defaultValue = "0") int page,
                                                           @RequestParam(name="size", defaultValue = "10") int size,
                                                           @RequestParam(name="sort", defaultValue = "createdDate,desc") String sort) {
        String[] sortParams = sort.split(",");
        Sort.Order order = new Sort.Order(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        PagingWishResponseDTO pagingWishResponseDTO  = wishService.getWishes(loginMemberDTO, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(pagingWishResponseDTO);
    }


    /*@PostMapping("/{optionId}")
    public ResponseEntity<String> addWish(@PathVariable("optionId") Long optionId,
                                          @LoginMember LoginMemberDTO loginMemberDTO) {
        System.out.println("post");
        wishService.addWish(optionId, loginMemberDTO);
        return ResponseEntity.ok("Wish added");
    }

    @GetMapping("")
    public ResponseEntity<List<WishResponseDTO>> getWishes(@LoginMember LoginMemberDTO loginMemberDTO) {
        System.out.println("get");
        List<WishResponseDTO> wishes = wishService.getWishes(loginMemberDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(wishes);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteWish(@PathVariable("productId") Long productId,
                                             @LoginMember LoginMemberDTO loginMemberDTO) {
        System.out.println("delete");
        wishService.removeWish(productId, loginMemberDTO);
        return ResponseEntity.ok("Wish deleted");
    }*/

    /*@GetMapping("/get-page/{memberId}")
    public ResponseEntity<Page<WishResponseDTO>> getPageById(@PageableDefault(page=1) Pageable pageable,
                                                             @PathVariable("memberId") Long memberId){
        System.out.println("get - page");
        Page<WishResponseDTO> wishPages = wishService.paging(pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(wishPages);
    }*/

}
