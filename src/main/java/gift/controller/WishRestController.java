package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.request.MemberRequest;
import gift.dto.request.WishRequest;
import gift.dto.response.WishResponse;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/wishes")
public class WishRestController {
    private final WishService wishService;

    public WishRestController(WishService wishService) {
        this.wishService = wishService;
    }

    @Operation(summary = "새로운 위시를 추가합니다")
    @PostMapping
    public ResponseEntity<Void> addWish(@LoginMember MemberRequest memberRequest, @RequestBody WishRequest wishRequest){
        wishService.save(memberRequest, wishRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "회원의 모든 위시 목록을 조회합니다")
    @GetMapping
    public ResponseEntity<Page<WishResponse>> getMemberWishes(@LoginMember MemberRequest memberRequest, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK)
                .body(wishService.getPagedMemberWishesByMemberId(memberRequest.id(),pageable));
    }

    @Operation(summary = "특정 위시를 삭제합니다")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeWish(@LoginMember MemberRequest memberRequest, @PathVariable Long id){
        wishService.deleteWishByMemberIdAndId(memberRequest.id(), id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "특정 위시의 수량을 업데이트합니다")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateQuantity(@LoginMember MemberRequest memberRequest, @PathVariable Long id, @RequestBody WishRequest wishRequest){
        wishService.updateQuantityByMemberIdAndId(memberRequest.id(), id, wishRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
