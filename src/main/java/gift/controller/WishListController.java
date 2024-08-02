package gift.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import gift.dto.response.GetWishListResponse;
import gift.dto.response.WishListPageResponse;
import gift.dto.response.WishListResponse;
import gift.exception.CustomException;
import gift.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


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

    @GetMapping("/page")
    @Operation(summary = "위시리스트 페이지 조회", description = "파라미터로 위시리스트 페이지를 반환합니다." )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "위시리스트 조회 성공"),
        @ApiResponse(responseCode = "401", description = "잘못된 토큰")
    })
    public ResponseEntity<List<WishListDto>> getWishListPage(@RequestHeader("Authorization") String authorizationHeader, @RequestBody MemberDto memberDto, 
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10")int size){
            if (!jwtUtil.validateToken(authorizationHeader)) {
                throw new CustomException("Unvalid Token", HttpStatus.UNAUTHORIZED, -40103);
            }

        WishListPageResponse wishListPageResponse = wishListService.findWishListById(jwtUtil.extractToken(authorizationHeader), page, size);
        return new ResponseEntity<>(wishListPageResponse.getWishLists(), HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "위시리스트 조회", description = "위시리스트를 조회합니다.")
    public ResponseEntity<GetWishListResponse> getWishList(@RequestHeader("Authorization") String authorizationHeader){
        if (!jwtUtil.validateToken(authorizationHeader)) {
            throw new CustomException("Unvalid Token", HttpStatus.UNAUTHORIZED, -40103);
        }
        return new ResponseEntity<>(wishListService.getWishList(jwtUtil.extractToken(authorizationHeader)), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "위시리스트 추가", description = "파라미터로 위시리스트를 추가합니다." )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "위시리스트 추가 성공"),
        @ApiResponse(responseCode = "401", description = "잘못된 토큰"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 상품"),
        @ApiResponse(responseCode = "409", description = "존재하는 위시리스트")
    })
    public ResponseEntity<WishListResponse> addWishList(@RequestHeader("Authorization") String authorizationHeader, @RequestBody WishListRequest wishListRequest){
        
        if (!jwtUtil.validateToken(authorizationHeader)) {
            throw new CustomException("Unvalid Token", HttpStatus.UNAUTHORIZED, -40103);
        }

        return new ResponseEntity<>(wishListService.addWishList(jwtUtil.extractToken(authorizationHeader), wishListRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("{wishListId}")
    @Operation(summary = "위시리스트 삭제", description = "파라미터로 위시리스트를 삭제합니다." )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "위시리스트 삭제 성공"),
        @ApiResponse(responseCode = "401", description = "잘못된 토큰"),
        @ApiResponse(responseCode = "403", description = "다른 사람의 위시리스트는 지울 수 없음"),
        @ApiResponse(responseCode = "404", description = "위시를 찾을 수 없음")
    })
    public ResponseEntity<Void> deleteWishList(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long wishListId){
        
        if (!jwtUtil.validateToken(authorizationHeader)) {
            throw new CustomException("Unvalid Token", HttpStatus.UNAUTHORIZED, -40103);
        }

        wishListService.deleteWishList(jwtUtil.extractToken(authorizationHeader), wishListId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
