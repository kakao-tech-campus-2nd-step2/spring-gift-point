package gift.controller.api;

import gift.dto.option.OptionRequest;
import gift.dto.option.OptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "상품 옵션 API")
public interface OptionApi {

    @Operation(summary = "상품에 옵션을 추가한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "옵션 추가 성공", content = @Content(schema = @Schema(implementation = OptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "409", description = "옵션 추가 실패(사유 : 이미 존재하는 이름입니다. )", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "내부 서버의 오류", content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<OptionResponse> addOption(Long productId, OptionRequest optionRequest);

    @Operation(summary = "기존 옵션을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "옵션 수정 성공"),
            @ApiResponse(responseCode = "400", description = "옵션 수정 실패(사유 : 옵션과 연결된 상품 ID 가 아닙니다.)"),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청"),
            @ApiResponse(responseCode = "409", description = "옵션 수정 실패(사유 : 이미 존재하는 이름입니다. )"),
            @ApiResponse(responseCode = "500", description = "내부 서버의 오류")
    })
    ResponseEntity<Void> updateOption(Long productId, Long id, OptionRequest optionRequest);

    @Operation(summary = "특정 옵션을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특정 옵션 조회 성공", content = @Content(schema = @Schema(implementation = OptionResponse.class))),
            @ApiResponse(responseCode = "400", description = "특정 옵션 조회 실패(사유 : 옵션과 연결된 상품 ID 가 아닙니다.)", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "내부 서버의 오류", content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<OptionResponse> getOption(Long productId, Long id);

    @Operation(summary = "모든 옵션을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 옵션 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = OptionResponse.class)))),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "내부 서버의 오류", content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<List<OptionResponse>> getOptions(Long productId);

    @Operation(summary = "특정 옵션을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "옵션 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "옵션 삭제 실패(사유 : 옵션과 연결된 상품 ID 가 아닙니다.)"),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청"),
            @ApiResponse(responseCode = "404", description = "옵션 삭제 실패(사유 : 존재하지 않는 ID 입니다.)"),
            @ApiResponse(responseCode = "500", description = "내부 서버의 오류")
    })
    ResponseEntity<Void> deleteOption(Long productId, Long id);
}
