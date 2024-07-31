package gift.controller;

import gift.common.annotation.LoginMember;
import gift.entity.Member;
import gift.dto.WishResponse;
import gift.dto.WishRequest;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
@Tag(name = "Wish API", description = "위시리스트 관련 API")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping
    @Operation(summary = "위시 리스트 상품 추가", description = "회원의 위시 리스트에 상품을 추가한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "성공"),
        @ApiResponse(responseCode = "-40101", description = "토큰을 찾을 수 없는 경우"),
        @ApiResponse(responseCode = "-40102", description = "토큰이 만료된 경우"),
        @ApiResponse(responseCode = "-40103", description = "토큰과 관련된 알 수 없는 오류"),
        @ApiResponse(responseCode = "-40401", description = "해당 ID의 상품이 존재하지 않음"),
        @ApiResponse(responseCode = "-40905", description = "위시리스트에 이미 상품이 존재함")
    })
    public ResponseEntity<Map<String, WishResponse>> addWish(@RequestBody WishRequest request,
        @LoginMember Member member) {
        WishResponse wishResponse = wishService.addWish(member, request);
        Map<String, WishResponse> response = new HashMap<>();
        response.put("wish", wishResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "위시 리스트 상품 조회", description = "회원의 위시 리스트에 있는 상품을 조회한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "-40101", description = "토큰을 찾을 수 없는 경우"),
        @ApiResponse(responseCode = "-40102", description = "토큰이 만료된 경우"),
        @ApiResponse(responseCode = "-40103", description = "토큰과 관련된 알 수 없는 오류")
    })
    public ResponseEntity<Map<String, List<WishResponse>>> getWishes(@LoginMember Member member) {
        List<WishResponse> wishList = wishService.getWishes(member).stream()
            .map(WishResponse::from)
            .toList();
        return ResponseEntity.ok(Map.of("wishlist", wishList));
    }

    @DeleteMapping("/{wishId}")
    @Operation(summary = "위시 리스트 상품 삭제", description = "회원의 위시 리스트에서 상품을 삭제한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "위시 삭제 성공"),
        @ApiResponse(responseCode = "-40101", description = "토큰을 찾을 수 없는 경우"),
        @ApiResponse(responseCode = "-40102", description = "토큰이 만료된 경우"),
        @ApiResponse(responseCode = "-40103", description = "토큰과 관련된 알 수 없는 오류"),
        @ApiResponse(responseCode = "-40302", description = "다른 멤버의 위시를 삭제하려고 함"),
        @ApiResponse(responseCode = "-40403", description = "해당 상품에 해당하는 위시가 존재하지 않음")
    })
    public ResponseEntity<Void> deleteWishById(@PathVariable Long wishId,
        @LoginMember Member member) {
        wishService.deleteWishById(member, wishId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
