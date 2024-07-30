package gift.wish;

import gift.common.auth.LoginMember;
import gift.common.auth.LoginMemberDto;
import gift.common.model.PageResponseDto;
import gift.wish.model.WishRequest;
import gift.wish.model.WishResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Wish API", description = "Wish 를 관리하는 API")
@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @Operation(summary = "전체 위시리스트 조회", description = "Token 을 기반으로 Wish List를 조회합니다.")
    @GetMapping
    public ResponseEntity<PageResponseDto<WishResponse>> getWishList(
        @LoginMember LoginMemberDto loginMemberDto,
        @PageableDefault(size = 10, sort = "product", direction = Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(
            PageResponseDto.of(wishService.getWishList(loginMemberDto, pageable), pageable));
    }

    @Operation(summary = "위시리스트 추가", description = "Token 을 기반으로 Wish List 에 Wish 를 추가합니다.")
    @PostMapping
    public ResponseEntity<Void> insertProductToWishList(@RequestBody WishRequest wishRequest,
        @LoginMember LoginMemberDto loginMemberDto) {
        Long wishId = wishService.addProductToWishList(wishRequest, loginMemberDto);
        return ResponseEntity.created(URI.create("/api/wishes/" + wishId)).build();
    }

    @Operation(summary = "특정 Wish 수정", description = "id에 해당하는 Wish 를 수정합니다.")
    @Parameter(name = "id", description = "수정할 wish 의 id")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProductInWishList(
        @PathVariable("id") Long wishId,
        @RequestBody WishRequest wishRequest,
        @LoginMember LoginMemberDto loginMemberDto) {
        wishService.updateProductInWishList(wishId, wishRequest, loginMemberDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "특정 Wish 삭제", description = "id에 해당하는 Wish 를 삭제합니다.")
    @Parameter(name = "id", description = "삭제할 wish 의 id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductInWishList(
        @PathVariable("id") Long wishId,
        @LoginMember LoginMemberDto loginMemberDto) {
        wishService.deleteProductInWishList(wishId, loginMemberDto);
        return ResponseEntity.ok().build();
    }
}
