package gift.controller;

import gift.entity.Member;
import gift.dto.WishResponse;
import gift.dto.WishRequest;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
@RequestMapping("/wishes")
@Tag(name = "Wish API", description = "위시리스트 관련 API")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping
    @Operation(summary = "위시리스트 추가", description = "새로운 위시를 추가합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = {@Content(schema = @Schema(implementation = WishResponse.class))}),
        @ApiResponse(responseCode = "400", description = "해당 ID의 상품이 존재하지 않습니다.")
    })
    public ResponseEntity<WishResponse> addWish(Member member, @RequestBody WishRequest request) {
        WishResponse wishResponse = wishService.addWish(member, request);
        return ResponseEntity.ok(wishResponse);
    }

    @GetMapping
    @Operation(summary = "위시리스트 조회", description = "회원의 모든 위시를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = {@Content(schema = @Schema(implementation = WishResponse.class))})
    })
    public ResponseEntity<List<WishResponse>> getWishes(Member member) {
        List<WishResponse> wishes = wishService.getWishes(member);
        return ResponseEntity.ok(wishes);
    }

    @GetMapping("/paged")
    @Operation(summary = "페이징된 위시리스트 조회", description = "페이지별로 위시를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = {@Content(schema = @Schema(implementation = WishResponse.class))})
    })
    public ResponseEntity<Slice<WishResponse>> getWishesPaged(Member member,
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<WishResponse> responseSlice = wishService.getWishes(member, pageable)
            .map(WishResponse::from);
        return ResponseEntity.ok(responseSlice);
    }

    @DeleteMapping("/prooductId/{productId}")
    @Operation(summary = "위시 삭제 (상품 ID)", description = "상품 ID로 특정 위시를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "위시 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "해당 상품에 해당하는 위시가 존재하지 않음")
    })
    public ResponseEntity<Void> deleteWishByProductName(Member member,
        @PathVariable Long productId) {
        wishService.deleteWishByProductId(member, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{wishId}")
    @Operation(summary = "위시 삭제 (위시 ID)", description = "위시 ID로 특정 위시를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "위시 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "해당 상품에 해당하는 위시가 존재하지 않음")
    })
    public ResponseEntity<Void> deleteWishById(@PathVariable Long wishId) {
        wishService.deleteWishById(wishId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
