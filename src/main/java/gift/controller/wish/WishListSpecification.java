package gift.controller.wish;

import gift.dto.gift.GiftResponse;
import gift.dto.paging.PagingRequest;
import gift.dto.paging.PagingResponse;
import gift.dto.wish.WishResponse;
import gift.model.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "위시리스트 관리", description = "위시리스트 관리를 위한 API")
@SecurityRequirement(name = "bearerAuth")
public interface WishListSpecification {

    @Operation(summary = "모든 상품 조회", description = "모든 상품을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "상품 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PagingResponse.class)
                            )
                    )
            })
    ResponseEntity<PagingResponse<GiftResponse>> getGiftList(@Parameter(description = "페이징 요청 정보") @ModelAttribute PagingRequest pagingRequest);

    @Operation(summary = "위시리스트에 상품 추가", description = "주어진 ID의 상품을 위시리스트에 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "위시리스트 추가 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(value = "{\"message\":\"위시리스트에 상품이 추가되었습니다.\"}")
                            )
                    )
            })
    ResponseEntity<String> addGiftToCart(@Parameter(hidden = true) @RequestAttribute("user") User user,
                                         @Parameter(description = "위시리스트에 추가할 상품의 ID") @PathVariable Long giftId,
                                         @RequestParam(required = false, defaultValue = "1") int quantity);

    @Operation(summary = "위시리스트 상품 수량 수정", description = "위시리스트에 있는 주어진 ID의 상품 수량을 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "상품 수량 수정 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(value = "{\"message\":\"위시리스트에서 상품 수량이 변경되었습니다.\"}")
                            )
                    )
            })
    ResponseEntity<String> updateGiftQuantity(@Parameter(hidden = true) @RequestAttribute("user") User user,
                                              @Parameter(description = "수량을 수정할 상품의 ID") @PathVariable Long giftId,
                                              @RequestParam(name = "quantity") int quantity);

    @Operation(summary = "위시리스트에서 상품 제거", description = "주어진 ID의 상품을 위시리스트에서 제거합니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "상품 제거 성공"),
                    @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
            })
    ResponseEntity<String> removeGiftFromCart(@Parameter(hidden = true) @RequestAttribute("user") User user,
                                              @Parameter(description = "제거할 상품의 ID") @PathVariable Long giftId);

    @Operation(summary = "사용자 위시리스트 조회", description = "로그인된 사용자의 위시리스트를 페이징하여 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "위시리스트 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PagingResponse.class)
                            )
                    )
            })
    ResponseEntity<PagingResponse<WishResponse>> getUserGifts(@Parameter(hidden = true) @RequestAttribute("user") User user,
                                                              @Parameter(description = "페이징 요청 정보") @ModelAttribute PagingRequest pagingRequest);
}
