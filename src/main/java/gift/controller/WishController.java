package gift.controller;

import gift.common.annotation.LoginMember;
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
import org.springframework.data.domain.Sort;
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
@Tag(name = "Wish API", description = "위시리스트 관련 API")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping
    @Operation(summary = "위시 리스트 상품 추가", description = "회원의 위시 리스트에 상품을 추가한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = {@Content(schema = @Schema(implementation = WishResponse.class))}),
        @ApiResponse(responseCode = "400", description = "해당 ID의 상품이 존재하지 않습니다.")
    })
    public ResponseEntity<WishResponse> addWish(@RequestBody WishRequest request,
        @LoginMember Member member) {
        WishResponse wishResponse = wishService.addWish(member, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(wishResponse);
    }

    @GetMapping
    @Operation(summary = "위시 리스트 상품 조회 (페이지네이션 적용)", description = "회원의 위시 리스트에 있는 상품을 페이지 단위로 조회한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = {@Content(schema = @Schema(implementation = WishResponse.class))})
    })
    public ResponseEntity<Slice<WishResponse>> getWishesPaged(@LoginMember Member member,
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdDate,desc") String[] sort) {
        Sort sorting = Sort.by(Sort.Order.by(sort[0]).with(Sort.Direction.fromString(sort[1])));
        Pageable pageable = PageRequest.of(page, size, sorting);
        Slice<WishResponse> responseSlice = wishService.getWishes(member, pageable)
            .map(WishResponse::from);
        return ResponseEntity.ok(responseSlice);
    }

    @DeleteMapping("/{wishId}")
    @Operation(summary = "위시 리스트 상품 삭제", description = "회원의 위시 리스트에서 상품을 삭제한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "위시 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "해당 상품에 해당하는 위시가 존재하지 않음")
    })
    public ResponseEntity<Void> deleteWishById(@PathVariable Long wishId) {
        wishService.deleteWishById(wishId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
