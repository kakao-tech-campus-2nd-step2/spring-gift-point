package gift.controller;

import gift.domain.Member;
import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishes")
@Tag(name = "Wish API", description = "위시리스트 API 관련 엔드포인트")
public class WishController {

    private WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }


    @GetMapping
    @Operation(summary = "위시리스트 조회", description = "회원의 위시리스트를 조회합니다.")
    public ResponseEntity<Page<WishResponse>> getWishes(@LoginMember Member member,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        Page<WishResponse> wishes = wishService.getWishes(member.getId(), PageRequest.of(page, size));

        return ResponseEntity.ok(wishes);
    }

    @PostMapping
    @Operation(summary = "위시리스트 추가", description = "회원의 위시리스트에 상품을 추가합니다.")
    public ResponseEntity<String> addWish(@RequestBody WishRequest wishRequest, @LoginMember Member member) {
        try {
            wishService.addWish(wishRequest, member.getId());
            return ResponseEntity.ok("위시리스트 담기 완료!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("위시리스트 담기 실패: " + e.getMessage());
        }

    }

    @DeleteMapping("/{wishId}")
    @Operation(summary = "위시리스트 삭제", description = "회원의 위시리스트에서 상품을 삭제합니다.")
    public ResponseEntity<Void> removeWish(@PathVariable Long wishId, @LoginMember Member member) {
        wishService.removeWish(wishId);
        return ResponseEntity.noContent().build();
    }

}
