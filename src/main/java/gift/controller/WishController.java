package gift.controller;

import gift.dto.WishRequest;
import gift.entity.Wish;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name="위시리스트 API")
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @Operation(summary = "위시리스트 조회")
    @ApiResponse(
            responseCode = "200",
            description = "성공적으로 위시리스트를 조회했습니다.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Wish.class),
                    examples = @ExampleObject(
                            value = "{\"content\":[{\"id\":1,\"productId\":1,\"userId\":1,\"createdDate\":\"2024-08-01T06:29:15Z\"}],\"pageable\":\"INSTANCE\",\"last\":false,\"totalPages\":1,\"totalElements\":1,\"size\":20,\"number\":0,\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"first\":true,\"numberOfElements\":1,\"empty\":false}"
                    )
            )
    )
    @GetMapping
    public ResponseEntity<Page<Wish>> getWishes(Pageable pageable) {
        Page<Wish> wishes = wishService.getWishes(pageable);
        return ResponseEntity.ok(wishes);
    }

    @Operation(summary = "위시리스트 추가")
    @ApiResponse(
            responseCode = "200",
            description = "성공적으로 위시리스트에 추가했습니다.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Wish.class),
                    examples = @ExampleObject(
                            value = "{\"id\":1,\"productId\":1,\"userId\":1,\"createdDate\":\"2024-08-01T06:29:15Z\"}"
                    )
            )
    )
    @Parameter(
            name = "wishRequest",
            description = "위시리스트 추가 요청",
            required = true,
            schema = @Schema(implementation = WishRequest.class)
    )
    @Parameter(
            name = HttpHeaders.AUTHORIZATION,
            description = "사용자 인증 토큰",
            required = false,
            example = "Bearer token"
    )
    @PostMapping
    public ResponseEntity<Wish> addWish(@RequestBody WishRequest wishRequest, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
        Wish wish = wishService.addWish(token, wishRequest);
        return ResponseEntity.ok(wish);
    }

    @Operation(summary = "위시리스트 삭제")
    @ApiResponse(
            responseCode = "200",
            description = "성공적으로 위시리스트에서 삭제했습니다."
    )
    @Parameter(
            name = "wishRequest",
            description = "위시리스트 삭제 요청",
            required = true,
            schema = @Schema(implementation = WishRequest.class)
    )
    @Parameter(
            name = HttpHeaders.AUTHORIZATION,
            description = "사용자 인증 토큰",
            required = false,
            example = "Bearer token"
    )
    @Parameter(
            name = "wishId",
            description = "삭제할 위시의 ID",
            required = true,
            example = "1"
    )
    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> removeWish(@RequestBody WishRequest wishRequest, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
        wishService.removeWish(token, wishRequest.getProductId());
        return ResponseEntity.ok().build();
    }
}