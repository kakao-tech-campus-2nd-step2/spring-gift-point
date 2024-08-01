package gift.controller;

import gift.domain.Member;
import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishes")
@Tag(name = "Wish API", description = "위시리스트 API 관련 엔드포인트")
public class WishController {

    private WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }


    @GetMapping
    @Operation(summary = "위시리스트 상품 조회", description = "회원의 위시리스트를 조회합니다.")
    public ResponseEntity<Page<WishResponse>> getWishes(@LoginMember Member member,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size,
                                                        @RequestParam(defaultValue = "createdDate,desc") String sort) {
        String[] sortParams = sort.split(",");
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]));

        Page<WishResponse> wishes = wishService.getWishes(member.getId(), pageRequest);

        return ResponseEntity.ok(wishes);
    }

    @PostMapping
    @Operation(summary = "위시리스트 상품 추가", description = "회원의 위시리스트에 상품을 추가합니다.")
    public ResponseEntity<String> addWish(@RequestBody WishRequest wishRequest, @LoginMember Member member) {
        try {
            wishService.addWish(wishRequest, member.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body("위시리스트 담기 완료!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("위시리스트 담기 실패: " + e.getMessage());
        }

    }

    @DeleteMapping("/{wishId}")
    @Operation(summary = "위시리스트 상품 삭제", description = "회원의 위시리스트에서 상품을 삭제합니다.")
    public ResponseEntity<String> removeWish(@PathVariable Long wishId, @LoginMember Member member) {
        try {
            wishService.removeWish(wishId);
            return ResponseEntity.ok("위시리스트 삭제 완료!");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

}
