package gift.controller;

import gift.LoginMember;
import gift.classes.RequestState.WishListPageRequestStateDTO;
import gift.classes.RequestState.WishListRequestStateDTO;
import gift.dto.MemberDto;
import gift.dto.RequestWishDto;
import gift.dto.WishDto;
import gift.dto.WishPageDto;
import gift.services.MemberService;
import gift.services.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
@Tag(name = "WishController", description = "Wish API")
public class WishController {

    public final WishService wishService;
    public final MemberService memberService;

    @Autowired
    public WishController(WishService wishService, MemberService memberService) {
        this.wishService = wishService;
        this.memberService = memberService;
    }

    //    Wish 추가
    @PostMapping
    @Operation(summary = "Wish 추가", description = "사용자의 Wish 리스트에 제품을 추가하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Wish 추가 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    public ResponseEntity<WishListRequestStateDTO> addWish(@LoginMember MemberDto memberDto,
        @RequestBody RequestWishDto requestWishDto) {

        WishDto wishDto = wishService.addWish(memberDto, requestWishDto.getProductId());
        return ResponseEntity.ok().body(new WishListRequestStateDTO(
            HttpStatus.OK,
            "위시리스트에 상품이 추가되었습니다.",
            wishDto
        ));
    }

    //    Wishlist 조회
    @GetMapping
    @Operation(summary = "Wishlist 조회", description = "사용자의 Wish 리스트를 조회하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Wishlist 조회 성공"),
        @ApiResponse(responseCode = "404", description = "Wishlist를 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    public ResponseEntity<WishListPageRequestStateDTO> getWishlistById(
        @LoginMember MemberDto memberDto,
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        WishPageDto wishPageDto = wishService.getWishListById(memberDto.getId(), page, size);
        return ResponseEntity.ok().body(new WishListPageRequestStateDTO(
            HttpStatus.OK,
            "위시 리스트가 조회되었습니다.",
            wishPageDto
        ));
    }

    //    Wish 삭제
    @DeleteMapping("/{wishId}")
    @Operation(summary = "Wish 삭제", description = "사용자의 Wish 리스트에서 제품을 삭제하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Wish 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "제품을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    public ResponseEntity<WishListRequestStateDTO> deleteWish(@LoginMember MemberDto memberDto,
        @PathVariable(value = "wishId") Long wishId) {
        wishService.deleteWish(wishId);

        return ResponseEntity.ok().body(new WishListRequestStateDTO(
            HttpStatus.OK,
            "위시 리스트가 삭제되었습니다.",
            null
        ));
    }
}
