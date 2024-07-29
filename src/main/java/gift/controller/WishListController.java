package gift.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import gift.service.WishListService;
import gift.dto.MemberDto;
import gift.dto.WishListDto;
import gift.dto.request.WishListRequest;
import gift.dto.response.WishListPageResponse;
import gift.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@Tag(name = "wishlist", description = "WishList API")
@RequestMapping("/wishlist")
public class WishListController {
    
    private final WishListService wishListService;
    private final JwtUtil jwtUtil;

    public WishListController(WishListService wishListService, JwtUtil jwtUtil){
        this.wishListService = wishListService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    @Operation(summary = "위시리스트 조회", description = "파라미터로 위시리스트 페이지를 반환합니다." )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "위시리스트 삭제 성공"),
        @ApiResponse(responseCode = "401", description = "잘못된 토큰")
    })
    public ResponseEntity<List<WishListDto>> getWishList(@RequestHeader("Authorization") String authorizationHeader, @RequestBody MemberDto memberDto, 
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10")int size){
        if (!jwtUtil.validateToken(authorizationHeader, memberDto)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        WishListPageResponse wishListPageResponse = wishListService.findWishListById(jwtUtil.extractToken(authorizationHeader), page, size);
        return new ResponseEntity<>(wishListPageResponse.getWishLists(), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "위시리스트 추가", description = "파라미터로 위시리스트를 추가합니다." )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "위시리스트 삭제 성공"),
        @ApiResponse(responseCode = "401", description = "잘못된 토큰"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 멤버 혹은 상품")
    })
    public ResponseEntity<Void> addWishList(@RequestHeader("Authorization") String authorizationHeader, @Valid @RequestBody WishListRequest wishListRequest, MemberDto memberDto){
        
        if (!jwtUtil.validateToken(authorizationHeader, memberDto)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        wishListService.addWishList(jwtUtil.extractToken(authorizationHeader), wishListRequest.getProductId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    @Operation(summary = "위시리스트 삭제", description = "파라미터로 위시리스트를 삭제합니다." )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "위시리스트 삭제 성공"),
        @ApiResponse(responseCode = "401", description = "잘못된 토큰")
    })
    public ResponseEntity<Void> deleteWishList(@RequestHeader("Authorization") String authorizationHeader, @Valid @RequestBody WishListRequest wishListRequest, MemberDto memberDto){
        
        if (!jwtUtil.validateToken(authorizationHeader, memberDto)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        wishListService.deleteWishList(jwtUtil.extractToken(authorizationHeader), wishListRequest.getProductId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
