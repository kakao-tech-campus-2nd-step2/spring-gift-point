package gift.wish.application;

import gift.auth.interceptor.Authorized;
import gift.auth.resolver.LoginMember;
import gift.member.domain.Role;
import gift.wish.application.dto.request.WishRequest;
import gift.wish.application.dto.response.WishPageResponse;
import gift.wish.application.dto.response.WishResponse;
import gift.wish.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Wish", description = "Wish관련 API")
@RestController
@RequestMapping("/api/wishes")
public class WishController {
    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @Operation(summary = "위시리스트 추가", description = "위시리스트를 추가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "위시리스트 추가 성공"),
            @ApiResponse(responseCode = "404", description = "일치하는 위시리스트를 찾을 수 없음")
    })
    @Authorized(Role.USER)
    @PostMapping()
    public ResponseEntity<Void> saveWish(@RequestBody WishRequest wishRequest,
                                         @LoginMember Long loginMemberId
    ) {
        var wishId = wishService.saveWish(wishRequest.toWishParam(loginMemberId));

        return ResponseEntity.created(URI.create("/api/wishes/" + wishId))
                .build();
    }

    @Operation(summary = "위시리스트 수정", description = "위시리스트를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "위시리스트 수정 성공"),
            @ApiResponse(responseCode = "404", description = "일치하는 위시리스트를 찾을 수 없음")
    })
    @PutMapping("/{wishId}")
    @Authorized(Role.USER)
    @ResponseStatus(HttpStatus.OK)
    public void modifyWish(@PathVariable("wishId") Long wishId,
                           @RequestBody WishRequest wishRequest,
                           @LoginMember Long loginMemberId
    ) {
        wishService.updateWish(wishRequest.toWishParam(loginMemberId), wishId);
    }

    @Operation(summary = "위시리스트 목록 조회", description = "위시리스트 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "위시리스트 목록 조회 성공"),
    })
    @GetMapping()
    @Authorized(Role.USER)
    public ResponseEntity<WishPageResponse> getWishList(Pageable pageable,
                                                        @LoginMember Long loginMemberId
    ) {
        var wishInfos = wishService.getWishList(loginMemberId, pageable);

        var response = WishPageResponse.from(wishInfos);
        return ResponseEntity.ok()
                .body(response);
    }

    @Operation(summary = "위시리스트 상세 조회", description = "위시리스트 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "위시리스트 상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "위시리스트를 찾을 수 없음")
    })
    @GetMapping("/{wishId}")
    @Authorized(Role.USER)
    public ResponseEntity<WishResponse> getWishDetail(@PathVariable("wishId") Long wishId,
                                                      @LoginMember Long loginMemberId
    ) {
        var wishInfo = wishService.getWish(wishId, loginMemberId);

        var response = WishResponse.from(wishInfo);
        return ResponseEntity.ok()
                .body(response);
    }

    @Operation(summary = "위시리스트 삭제", description = "위시리스트를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "위시리스트 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "위시리스트를 찾을 수 없음")
    })
    @DeleteMapping("/{wishId}")
    @Authorized(Role.USER)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWish(@PathVariable("wishId") Long wishId,
                           @LoginMember Long loginMemberId
    ) {
        wishService.deleteWish(wishId, loginMemberId);
    }
}
