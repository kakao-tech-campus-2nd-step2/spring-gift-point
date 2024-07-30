package gift.controller;

import gift.domain.Wish;
import gift.domain.Wish.wishDetail;
import gift.domain.Wish.wishSimple;
import gift.service.WishService;
import gift.util.ParsingPram;
import gift.util.page.PageMapper;
import gift.util.page.PageResult;
import gift.util.page.SingleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저의 관심목록 관련 서비스")
@RestController
@RequestMapping("/api/wish")
public class WishController {

    private final WishService wishListService;
    private final ParsingPram parsingPram;

    @Autowired
    public WishController(WishService wishListService, ParsingPram parsingPram) {
        this.wishListService = wishListService;
        this.parsingPram = parsingPram;
    }

    //    user id로 위시리스트 반환
    //    user id 검증
    @Operation(summary = "유저 위시 리스트 조회", description = "위시리스트 id, 유저id, 상품id 반환")
    @GetMapping
    public PageResult<wishSimple> getWishList(HttpServletRequest req,
        @Valid Wish.getList pram) {
        return PageMapper.toPageResult(wishListService.getWishList(parsingPram.getId(req), pram));
    }

    //    Wish id로 상세정보 반환
    //    wish id 검증
    @Operation(summary = "유저 위시 조회", description = "위시리스트 id, 유저id, 상품id, 상품명, 상품가격, 상품 이미지 반환")
    @GetMapping("/{id}")
    public SingleResult<wishDetail> getWish(@PathVariable long id) {
        return new SingleResult(wishListService.getWish(id));
    }

    //  위시리스트 추가
    //  user id 검증, product id 검증,  위시 리스트내 중복여부 검증
    @Operation(summary = "유저 위시 추가")
    @PostMapping
    public SingleResult<Long> createWish(HttpServletRequest req,
        @Valid @RequestBody Wish.createWish create) {
        return new SingleResult(wishListService.createWish(parsingPram.getId(req), create));
    }

    //    wish id로 위시리스트 삭제
    //    wish id 검증
    @Operation(summary = "유저 위시 삭제")
    @DeleteMapping("/{id}")
    public SingleResult<Long> deleteWish(@PathVariable long id) {
        return new SingleResult(wishListService.deleteWish(id));
    }

}
