package gift.controller.api;

import gift.dto.option.OptionAddRequest;
import gift.dto.option.OptionResponse;
import gift.dto.option.OptionUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "상품 옵션 API")
public interface OptionApi {

    @Operation(summary = "상품에 옵션을 추가한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "옵션 추가 성공"),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청")
    })
    ResponseEntity<Void> addOption(OptionAddRequest optionAddRequest);

    @Operation(summary = "기존 옵션을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "옵션 수정 성공"),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청")
    })
    ResponseEntity<Void> updateOption(Long id, OptionUpdateRequest optionUpdateRequest);

    @Operation(summary = "모든 옵션을 페이지 단위로 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 옵션 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = OptionResponse.class)))),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청")
    })
    ResponseEntity<List<OptionResponse>> getOptions(Long productId, Pageable pageable);

    @Operation(summary = "특정 옵션을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "옵션 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청"),
            @ApiResponse(responseCode = "404", description = "옵션 삭제 실패(사유 : 존재하지 않는 ID 입니다.)")
    })
    ResponseEntity<Void> deleteOption(Long id);
}
