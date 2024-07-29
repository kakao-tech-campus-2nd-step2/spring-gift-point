package gift.controller.wish;

import gift.config.LoginAdmin;
import gift.config.LoginUser;
import gift.controller.auth.AuthController;
import gift.controller.auth.LoginResponse;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Wish", description = "Wish API")
@RequestMapping("api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    @Operation(summary = "get All wishes", description = "모든 위시리스트 조회")
    public ResponseEntity<Page<WishResponse>> getAllWishes(
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(wishService.findAll(pageable));
    }

    @GetMapping("/{memberId}")
    @Operation(summary = "get All wishes by member", description = "멤버의 모든 위시리스트 조회")
    public ResponseEntity<Page<WishResponse>> getWishes(@PathVariable UUID memberId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK)
            .body(wishService.findAllByMemberId(memberId, pageable));
    }

    @PostMapping("/{memberId}")
    @Operation(summary = "create wish", description = "위시리스트 생성")
    public ResponseEntity<WishResponse> createWish(@LoginUser LoginResponse member,
        @PathVariable UUID memberId, @RequestBody WishCreateRequest wish) {
        return ResponseEntity.status(HttpStatus.CREATED).body(wishService.save(memberId, wish));
    }

    @PutMapping("/{memberId}/{productId}")
    @Operation(summary = "modify wish", description = "위시리스트 수정")
    public ResponseEntity<WishResponse> updateWish(@LoginAdmin LoginResponse member,
        @PathVariable UUID memberId, @PathVariable UUID productId,
        @RequestBody WishUpdateRequest wish) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(wishService.update(memberId, productId, wish));
    }

    @DeleteMapping("/{memberId}/{productId}")
    @Operation(summary = "delete wish", description = "위시리스트 삭제")
    public ResponseEntity<Void> deleteProduct(@LoginUser LoginResponse member,
        @PathVariable UUID memberId, @PathVariable UUID productId) {
        AuthController.validateUserOrAdmin(member, memberId);
        wishService.delete(memberId, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}