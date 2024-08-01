package gift.controller;

import gift.authentication.LoginMember;
import gift.authentication.UserDetails;
import gift.dto.SuccessResponse;
import gift.dto.WishAddRequestDto;
import gift.dto.WishResponseDto;
import gift.dto.WishUpdateRequestDto;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Wish", description = "사용자 위시 관련 API")
@RestController
@RequestMapping("/api/wishes")
public class WishController {
    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @Operation(summary = "위시 등록 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "위시 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public ResponseEntity<SuccessResponse> addNewWish(@LoginMember UserDetails userDetails,
                                                      @RequestBody WishAddRequestDto request) {
        wishService.addWish(userDetails.id(), request);
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "위시 리스트 등록에 성공하였습니다."));
    }

    @Operation(summary = "위시 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "위시 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping
    public ResponseEntity<List<WishResponseDto>> getMemberWishList(
            @LoginMember UserDetails userDetails,
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNum,
            @RequestParam(required = false, defaultValue = "10", value = "size") int size,
            @RequestParam(required = false, defaultValue = "id", value = "criteria") String criteria) {
        Pageable pageable = PageRequest.of(pageNum, size, Sort.by(Sort.Direction.ASC, criteria));
        return ResponseEntity.ok(wishService.getAllWishes(userDetails.id(), pageable));
    }

    @Operation(summary = "위시 수량 수정 API(사용하지 않는 API 삭제 예정)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "위시 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse> updateWish(
            @LoginMember UserDetails userDetails,
            @PathVariable("id") Long id,
            @RequestBody WishUpdateRequestDto requestDto) {
        wishService.updateWish(userDetails.id(), id, requestDto.quantity());
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.OK, "수량이 성공적으로 수정되었습니다."));
    }

    @Operation(summary = "위시 삭제 API")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "위시 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteWish(
            @LoginMember UserDetails userDetails,
            @PathVariable("id") Long productId) {
        wishService.deleteWish(userDetails.id(), productId);
        return ResponseEntity.ok(new SuccessResponse(HttpStatus.NO_CONTENT, "성공적으로 삭제되었습니다."));
    }
}
