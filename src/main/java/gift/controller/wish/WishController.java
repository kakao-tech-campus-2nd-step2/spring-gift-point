package gift.controller.wish;

import gift.config.LoginUser;
import gift.controller.auth.AuthController;
import gift.controller.auth.LoginResponse;
import gift.controller.response.ApiResponseBody;
import gift.controller.response.ApiResponseBuilder;
import gift.controller.response.PageInfo;
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
    public ResponseEntity<ApiResponseBody<WishPageResponse>> getAllWishes(
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        var targets = wishService.findAll(pageable);
        PageInfo pageInfo = new PageInfo(targets.getPageable().getPageNumber(),
            targets.getTotalElements(), targets.getTotalPages());
        return new ApiResponseBuilder<WishPageResponse>()
            .httpStatus(HttpStatus.OK)
            .data(new WishPageResponse(pageInfo, targets.toList()))
            .messages("모든 위시리스트 조회")
            .build();
    }

    @GetMapping("/{memberId}")
    @Operation(summary = "get All wishes by member", description = "멤버의 모든 위시리스트 조회")
    public ResponseEntity<ApiResponseBody<Page<WishResponse>>> getWishes(
        @PathVariable UUID memberId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ApiResponseBuilder<Page<WishResponse>>()
            .httpStatus(HttpStatus.OK)
            .data(wishService.findAllByMemberId(memberId, pageable))
            .messages("위시리스트 조회")
            .build();
    }

    @PostMapping("/{memberId}")
    @Operation(summary = "create wish", description = "위시리스트 생성")
    public ResponseEntity<ApiResponseBody<WishResponse>> createWish(@LoginUser LoginResponse member,
        @PathVariable UUID memberId, @RequestBody WishCreateRequest wish) {
        return new ApiResponseBuilder<WishResponse>()
            .httpStatus(HttpStatus.OK)
            .data(wishService.save(memberId, wish))
            .messages("위시리스트 생성")
            .build();
    }

    @DeleteMapping("/{memberId}/{productId}")
    @Operation(summary = "delete wish", description = "위시리스트 삭제")
    public ResponseEntity<ApiResponseBody<Void>> deleteProduct(@LoginUser LoginResponse member,
        @PathVariable UUID memberId, @PathVariable UUID productId) {
        AuthController.validateUserOrAdmin(member, memberId);
        wishService.delete(memberId, productId);
        return new ApiResponseBuilder<Void>()
            .httpStatus(HttpStatus.OK)
            .data(null)
            .messages("위시리스트 삭제")
            .build();
    }
}