package gift.controller;

import gift.constants.ResponseMsgConstants;
import gift.dto.betweenClient.option.OptionRequestDTO;
import gift.dto.betweenClient.ResponseDTO;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Validated
@RequestMapping("/api/products/{productId}/options")
public class OptionController {

    private final OptionService optionService;

    @Autowired
    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    @Operation(description = "서버가 특정 상품의 모든 옵션을 제공합니다.", tags = "Option")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 옵션들을 제공합니다.",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OptionRequestDTO.class)))),
            @ApiResponse(responseCode = "500", description = "서버에 의한 오류입니다",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))})
    public ResponseEntity<?> getOneProductIdAllOptions(@PathVariable Long productId) {
        return new ResponseEntity<>(optionService.getOneProductIdAllOptions(productId), HttpStatus.OK);
    }

    @PostMapping
    @Operation(description = "서버가 클라이언트가 제출한 옵션을 추가합니다.", tags = "Option")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "옵션 추가에 성공했습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class),
                            examples = @ExampleObject(value = "{\"isError\": false, \"message\": \"success\"}"))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 요청 양식이 잘못되거나, 옵션 이름이 중복된 경우입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버에 의한 오류입니다",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))})
    public ResponseEntity<ResponseDTO> addOption(@PathVariable Long productId, @Valid @RequestBody OptionRequestDTO optionRequestDTO) {
        optionService.addOption(productId, optionRequestDTO);
        return new ResponseEntity<>(new ResponseDTO(false, ResponseMsgConstants.WELL_DONE_MESSAGE), HttpStatus.CREATED);
    }

    @PutMapping("/{optionId}")
    @Operation(description = "서버가 클라이언트가 요청한 옵션 ID를 수정합니다.", tags = "Option")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "옵션 수정에 성공했습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class),
                            examples = @ExampleObject(value = "{\"isError\": false, \"message\": \"success\"}"))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 보통 요청 양식이 잘못되었거나, 존재하지 않는 옵션 ID이거나, 옵션 이름이 중복된 경우입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버에 의한 오류입니다",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))})
    public ResponseEntity<ResponseDTO> updateOption(@PathVariable Long productId, @PathVariable Long optionId, @RequestBody OptionRequestDTO optionRequestDTO) {
        optionService.updateOption(productId, optionId, optionRequestDTO);
        return new ResponseEntity<>(new ResponseDTO(false, ResponseMsgConstants.WELL_DONE_MESSAGE), HttpStatus.OK);
    }

    @DeleteMapping("/{optionId}")
    @Operation(description = "서버가 클라이언트가 제출한 옵션 ID로 옵션을 삭제합니다.", tags = "Option")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "옵션 삭제에 성공했습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class),
                            examples = @ExampleObject(value = "{\"isError\": false, \"message\": \"success\"}"))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 보통 존재하지 않는 옵션 ID인 경우입니다",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버에 의한 오류입니다",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))})
    public ResponseEntity<ResponseDTO> deleteOption(@PathVariable Long productId, @PathVariable Long optionId) {
        optionService.deleteOption(productId, optionId);
        return new ResponseEntity<>(new ResponseDTO(false, ResponseMsgConstants.WELL_DONE_MESSAGE), HttpStatus.NO_CONTENT);
    }
}
