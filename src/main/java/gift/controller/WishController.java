package gift.controller;

import gift.dto.request.LoginMemberDTO;
import gift.dto.request.TokenLoginRequestDTO;
import gift.dto.response.WishResponseDTO;
import gift.service.LoginMember;
import gift.service.WishService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member/{memberId}/wishes")
public class WishController {
    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping("/{optionId}")
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
    }

    /*@GetMapping("/get-page/{memberId}")
    public ResponseEntity<Page<WishResponseDTO>> getPageById(@PageableDefault(page=1) Pageable pageable,
                                                             @PathVariable("memberId") Long memberId){
        System.out.println("get - page");
        Page<WishResponseDTO> wishPages = wishService.paging(pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(wishPages);
    }*/

}
