package gift.controller.gift;

import gift.dto.gift.GiftRequest;
import gift.dto.gift.GiftResponse;
import gift.dto.paging.PagingRequest;
import gift.dto.paging.PagingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "상품 관리", description = "상품 관리를 위한 API")
public interface GiftSpecification {

    @Operation(summary = "새 상품 추가", description = "새로운 상품을 등록하고 성공 메시지를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "상품 생성 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(value = "{\"message\":\"상품이 생성되었습니다.\"}")
                            )
                    )
            })
    ResponseEntity<String> addGift(@Valid @RequestBody GiftRequest.Create giftRequest);

    @Operation(summary = "상품 조회", description = "주어진 ID에 해당하는 상품을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "상품 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GiftResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
            })
    ResponseEntity<GiftResponse> getGift(@Parameter(description = "조회할 상품의 ID") @PathVariable Long id);

    @Operation(summary = "모든 상품 조회", description = "모든 상품을 페이징하여 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "상품 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PagingResponse.class)
                            )
                    )
            })
    ResponseEntity<PagingResponse<GiftResponse>> getAllGift(@Parameter(description = "페이징 요청 정보") @ModelAttribute PagingRequest pagingRequest);

    @Operation(summary = "상품 수정", description = "주어진 ID에 해당하는 상품을 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "상품 수정 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(value = "{\"message\":\"상품 수정이 완료되었습니다.\"}")
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
            })
    ResponseEntity<String> updateGift(@Parameter(description = "수정할 상품의 ID") @PathVariable Long id,
                                      @RequestBody GiftRequest.Update giftRequest);

    @Operation(summary = "상품 삭제", description = "주어진 ID에 해당하는 상품을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "상품 삭제 성공"),
                    @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
            })
    ResponseEntity<String> deleteGift(@Parameter(description = "삭제할 상품의 ID") @PathVariable Long id);
}
