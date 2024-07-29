package gift.domain.wishlist.controller;

import gift.annotation.LoginMember;
import gift.domain.member.entity.Member;
import gift.domain.wishlist.dto.ProductIdRequest;
import gift.domain.wishlist.dto.WishRequest;
import gift.domain.wishlist.dto.WishResponse;
import gift.domain.wishlist.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
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
@Tag(name = "WishController", description = "WishController API(JWT 인증 필요)")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    @Operation(summary = "전체 위시리스트 조회", description = "전체 위시리스트 조회합니다.")
    @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    @Parameters({
        @Parameter(name = "pageNo", description = "페이지 번호 (0부터 시작)", example = "0"),
        @Parameter(name = "pageSize", description = "페이지 크기", example = "10")
    })
    public ResponseEntity<Page<WishResponse>> getWishes(
        @Parameter(hidden = true) @LoginMember Member member,
        @RequestParam(defaultValue = "0") int pageNo,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        Page<WishResponse> response = wishService.getWishesByMember(member, pageNo, pageSize);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "위시리스트 생성", description = "위시 리스트를 생성합니다.")
    @ApiResponse(responseCode = "201", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json"))
    public ResponseEntity<WishResponse> createWish(@RequestBody ProductIdRequest productIdRequest,
        @Parameter(hidden = true) @LoginMember Member member) {
        WishRequest wishRequest = new WishRequest(member.getId(), productIdRequest.getProductId());
        WishResponse response = wishService.createWish(wishRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "위시 리스트 삭제", description = "해당 위시 리스트를 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "요청에 성공하였습니다.")
    @Parameter(name = "id", description = "삭제할 위시 리스트의 ID", example = "1")
    public ResponseEntity<WishResponse> deleteWish(@PathVariable("id") Long id,
        @Parameter(hidden = true) @LoginMember Member member) {
        wishService.deleteWish(id, member);

        return ResponseEntity.noContent().build();
    }
}