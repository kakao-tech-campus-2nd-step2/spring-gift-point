package gift.wish.controller;

import gift.annotation.LoginUser;
import gift.config.PageConfig;
import gift.user.entity.User;
import gift.wish.dto.request.CreateWishRequest;
import gift.wish.dto.request.UpdateWishRequest;
import gift.wish.dto.response.WishResponse;
import gift.wish.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/wishes")
@Tag(name = "위시리스트 관리", description = "위시리스트를 관리하는 API")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    @Operation(summary = "위시리스트 조회", description = "유저의 위시리스트를 조회합니다")
    @Parameter(name = "Authorization", description = "Bearer jwt 토큰")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "404", description = "위시리스트가 존재하지 않음")
    })
    public ResponseEntity<Page<WishResponse>> getWishes(
        @LoginUser User user,
        @PageableDefault(
            size = PageConfig.PAGE_PER_COUNT,
            sort = PageConfig.SORT_STANDARD,
            direction = Direction.DESC
        ) Pageable pageable
    ) {
        return ResponseEntity.ok(wishService.getWishes(user.getId(), pageable));
    }

    @PostMapping
    @Operation(summary = "위시 추가", description = "유저에게 새로운 위시를 추가합니다")
    @Parameter(name = "Authorization", description = "Bearer jwt 토큰")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "성공",
            headers = {@Header(name = "location", description = "위시가 생성된 위치 엔드포인트")}),
        @ApiResponse(responseCode = "400", description = "잘못된 요청으로 인한 오류 발생")
    })
    public ResponseEntity<WishResponse> addWish(
        @LoginUser User user, @RequestBody @Valid CreateWishRequest request
    ) {
        WishResponse response = wishService.createWish(user.getId(), request);
        URI location = UriComponentsBuilder.fromPath("/api/wishes/{id}")
            .buildAndExpand(response.id())
            .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PatchMapping
    @Operation(summary = "위시 수정", description = "유저의 {id} 위시를 수정합니다")
    @Parameter(name = "Authorization", description = "Bearer jwt 토큰")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "위시리스트가 존재하지 않음")
    })
    public ResponseEntity<Void> updateWishes(
        @LoginUser User user, @RequestBody List<UpdateWishRequest> requests
    ) {
        wishService.updateWishes(requests);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping({"id"})
    @Operation(summary = "위시 삭제", description = "유저의 {id} 위시를 삭제합니다")
    @Parameter(name = "Authorization", description = "Bearer jwt 토큰")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "위시리스트가 존재하지 않음")
    })
    public ResponseEntity<Void> deleteWishes(
        @LoginUser User user, @RequestParam Long id
    ) {
        wishService.deleteWish(id);
        return ResponseEntity.ok().build();
    }
}
