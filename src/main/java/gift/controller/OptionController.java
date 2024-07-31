package gift.controller;

import gift.constants.ResponseMsgConstants;
import gift.dto.betweenClient.option.OptionRequestDTO;
import gift.dto.betweenClient.ResponseDTO;
import gift.dto.betweenClient.option.OptionResponseDTO;
import gift.dto.swagger.GetOneProductIdAllOptions;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetOneProductIdAllOptions.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 요청 양식이 잘못되거나, 해당 상품 아이디가 존재하지 않는 경우입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버에 의한 오류입니다",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))})
    public ResponseEntity<Map<String, List<OptionResponseDTO>>> getOneProductIdAllOptions(@PathVariable Long productId) {
        Map<String, List<OptionResponseDTO>> response = new HashMap<>();
        response.put("options", optionService.getOneProductIdAllOptions(productId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @Hidden
    public ResponseEntity<ResponseDTO> addOption(@PathVariable Long productId, @Valid @RequestBody OptionRequestDTO optionRequestDTO) {
        optionService.addOption(productId, optionRequestDTO);
        return new ResponseEntity<>(new ResponseDTO(false, ResponseMsgConstants.WELL_DONE_MESSAGE), HttpStatus.CREATED);
    }

    @PutMapping("/{optionId}")
    @Hidden
    public ResponseEntity<ResponseDTO> updateOption(@PathVariable Long productId, @PathVariable Long optionId, @RequestBody OptionRequestDTO optionRequestDTO) {
        optionService.updateOption(productId, optionId, optionRequestDTO);
        return new ResponseEntity<>(new ResponseDTO(false, ResponseMsgConstants.WELL_DONE_MESSAGE), HttpStatus.OK);
    }

    @DeleteMapping("/{optionId}")
    @Hidden
    public ResponseEntity<ResponseDTO> deleteOption(@PathVariable Long productId, @PathVariable Long optionId) {
        optionService.deleteOption(productId, optionId);
        return new ResponseEntity<>(new ResponseDTO(false, ResponseMsgConstants.WELL_DONE_MESSAGE), HttpStatus.NO_CONTENT);
    }
}
