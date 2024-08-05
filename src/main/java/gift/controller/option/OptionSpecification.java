package gift.controller.option;

import gift.dto.option.OptionRequest;
import gift.dto.option.OptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "옵션 관리", description = "상품 옵션 관리를 위한 API")
public interface OptionSpecification {

    @Operation(summary = "모든 옵션 조회", description = "모든 옵션을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "옵션 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OptionResponse.class)
                            )
                    )
            })
    ResponseEntity<OptionResponse.InfoList> getAllOptions();

    @Operation(summary = "상품의 모든 옵션 조회", description = "주어진 상품 ID에 해당하는 모든 옵션을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "옵션 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OptionResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
            })
    ResponseEntity<OptionResponse.InfoList> getAllOptionsFromGift(@Parameter(description = "조회할 상품의 ID") @PathVariable Long id);

    @Operation(summary = "상품에 옵션 추가", description = "주어진 상품 ID에 새로운 옵션을 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "옵션 추가 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(value = "{\"message\":\"옵션이 상품에 추가되었습니다!\"}")
                            )
                    )
            })
    ResponseEntity<String> addOptionToGift(@Parameter(description = "옵션을 추가할 상품의 ID") @PathVariable("id") Long giftId,
                                           @Valid @RequestBody OptionRequest.Create optionRequest);

    @Operation(summary = "상품의 옵션 수정", description = "주어진 상품 ID와 옵션 ID에 해당하는 옵션을 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "옵션 수정 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(value = "{\"message\":\"옵션이 수정되었습니다!\"}")
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "옵션을 찾을 수 없음")
            })
    ResponseEntity<String> updateOptionToGift(@Parameter(description = "옵션을 수정할 상품의 ID") @PathVariable("giftId") Long giftId,
                                              @Parameter(description = "수정할 옵션의 ID") @PathVariable("optionId") Long optionId,
                                              @Valid @RequestBody OptionRequest.Update optionRequest);

    @Operation(summary = "상품의 옵션 수량 차감", description = "주어진 상품 ID와 옵션 ID에 해당하는 옵션의 수량을 차감합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "옵션 수량 차감 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(value = "{\"message\":\"옵션 수량이 차감되었습니다!\"}")
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "옵션을 찾을 수 없음")
            })
    ResponseEntity<String> subtractOptionToGift(@Parameter(description = "옵션 수량을 차감할 상품의 ID") @PathVariable("giftId") Long giftId,
                                                @Parameter(description = "수량을 차감할 옵션의 ID") @PathVariable("optionId") Long optionId,
                                                @RequestParam(name = "quantity") int quantity);

    @Operation(summary = "상품에서 옵션 삭제", description = "주어진 상품 ID와 옵션 ID에 해당하는 옵션을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "옵션 삭제 성공"),
                    @ApiResponse(responseCode = "404", description = "옵션을 찾을 수 없음")
            })
    ResponseEntity<String> deleteOptionFromGift(@Parameter(description = "옵션을 삭제할 상품의 ID") @PathVariable("giftId") Long giftId,
                                                @Parameter(description = "삭제할 옵션의 ID") @PathVariable("optionId") Long optionId);
}