package gift.api.wishlist.controller;

import gift.api.wishlist.dto.WishRequest;
import gift.api.wishlist.dto.WishResponse;
import gift.api.wishlist.service.WishService;
import gift.global.resolver.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
@Tag(name = "Wish", description = "Wish List API")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    @Operation(summary = "위시리스트 조회", description = "멤버의 위시리스트 페이지별 조회")
    public ResponseEntity<List<WishResponse>> getItems(@LoginMember Long memberId, Pageable pageable) {
        return ResponseEntity.ok().body(wishService.getItems(memberId, pageable));
    }

    @PostMapping
    @Operation(summary = "위시리스트 추가", description = "위시리스트에 상품 추가")
    public ResponseEntity<Void> add(@RequestBody @Valid WishRequest wishRequest, @LoginMember Long memberId) {
        wishService.add(memberId, wishRequest);
        return ResponseEntity.created(URI.create("/api/wishes/" + memberId)).build();
    }

    @Deprecated
    @PutMapping
    @Operation(summary = "위시리스트 수정", description = "위시리스트 상품 수량 수정")
    public ResponseEntity<Void> update(@RequestBody @Valid WishRequest wishRequest, @LoginMember Long memberId) {
        wishService.update(memberId, wishRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "위시리스트 삭제", description = "위시리스트 상품 삭제")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id, @LoginMember Long memberId) {
        wishService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
