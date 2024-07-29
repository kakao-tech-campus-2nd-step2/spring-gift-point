package gift.product.docs;

import gift.product.dto.OptionDTO;
import gift.product.model.Option;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

@Tag(name = "Option", description = "API 컨트롤러")
public interface OptionControllerDocs {

    @Operation(summary = "옵션 목록", description = "특정 상품에 등록된 옵션 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "옵션 목록 정상 출력",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "401", description = "인증과 관련된 문제 발생(인증 헤더 누락 또는 토큰 인증 실패)")})
    Page<?> getAllOptions(Long productId, Pageable pageable);

    @Operation(summary = "옵션 등록", description = "특정 상품에 옵션을 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "옵션 등록 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Option.class))),
        @ApiResponse(responseCode = "400", description = "등록하고자 하는 옵션의 정보가 올바르게 입력되지 않은 경우"),
        @ApiResponse(responseCode = "401", description = "인증과 관련된 문제(인증 헤더 누락 또는 토큰 인증 실패)가 발생한 경우"),
        @ApiResponse(responseCode = "409", description = "동일 상품 내 기등록된 옵션의 이름과 동일한 이름의 옵션의 등록을 시도한 경우")})
    ResponseEntity<?> registerOption(Long productId, OptionDTO optionDTO, BindingResult bindingResult);

    @Operation(summary = "옵션 수정", description = "기등록된 옵션을 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "옵션 수정 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Option.class))),
        @ApiResponse(responseCode = "400", description = "수정하고자 하는 옵션의 정보가 올바르게 입력되지 않은 경우"),
        @ApiResponse(responseCode = "401", description = "인증과 관련된 문제(인증 헤더 누락 또는 토큰 인증 실패)가 발생한 경우"),
        @ApiResponse(responseCode = "404", description = "등록되지 않은 옵션의 수정을 시도한 경우"),
        @ApiResponse(responseCode = "409", description = "기등록된 옵션의 이름과 동일한 이름의 옵션의 수정을 시도한 경우")})
    ResponseEntity<?> updateOption(Long id, Long productId, OptionDTO optionDTO, BindingResult bindingResult);

    @Operation(summary = "옵션 삭제", description = "옵션을 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "옵션 삭제 성공"),
        @ApiResponse(responseCode = "401", description = "인증과 관련된 문제(인증 헤더 누락 또는 토큰 인증 실패)가 발생한 경우"),
        @ApiResponse(responseCode = "403", description = "상품에 마지막 남은 옵션의 삭제를 시도한 경우"),
        @ApiResponse(responseCode = "404", description = "등록되지 않은 옵션의 삭제를 시도한 경우")})
    ResponseEntity<Void> deleteOption(Long id, Long productId);
}
