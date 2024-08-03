package gift.controller;

import gift.annotation.LoginUser;

import gift.dto.option.OptionRequestDTO;
import gift.dto.option.OptionResponseDTO;
import gift.dto.option.OptionsResponseDTO;

import gift.model.User;
import gift.service.OptionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/products")
public class OptionController {
    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/{productId}/options")
    @Operation(summary = "상품 옵션 목록 조회",
            description = "지정된 상품의 옵션 목록을 조회합니다.",
            responses = {
                @ApiResponse(responseCode = "200", description = "상품 옵션 목록 조회 성공"),
                @ApiResponse(responseCode = "404", description = "존재하지 않는 productId"),
                @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<OptionsResponseDTO> getOptions(@PathVariable(name = "productId") Long productId) {
        OptionsResponseDTO options = optionService.getProductOptions(productId);

        return new ResponseEntity<>(options, HttpStatus.OK);
    }

    @PostMapping("/{productId}/options")
    @Operation(summary = "상품 옵션 추가",
            description = "지정된 상품에 새로운 옵션을 추가합니다.",
            responses = {
                @ApiResponse(responseCode = "201", description = "상품 옵션 추가 성공"),
                @ApiResponse(responseCode = "400", description = "잘못 된 값 입력"),
                @ApiResponse(responseCode = "401", description = "인증 오류"),
                @ApiResponse(responseCode = "404", description = "존재하지 않는 productId"),
                @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<OptionResponseDTO> addOption(@LoginUser User user, @PathVariable(name = "productId") Long productId, @Valid @RequestBody OptionRequestDTO optionRequestDTO) {
        OptionResponseDTO optionResponseDTO = optionService.createOption(productId, optionRequestDTO);

        return new ResponseEntity<>(optionResponseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}/options/{optionId}")
    @Operation(summary = "상품 옵션 수정",
            description = "지정된 상품 옵션을 수정합니다.",
            responses = {
                @ApiResponse(responseCode = "200", description = "상품 옵션 수정 성공"),
                @ApiResponse(responseCode = "400", description = "잘못 된 값 입력"),
                @ApiResponse(responseCode = "401", description = "인증 오류"),
                @ApiResponse(responseCode = "404", description = "존재하지 않는 optionId"),
                @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<OptionResponseDTO> modifyOption(@LoginUser User user,
                                                          @PathVariable Long productId,
                                                          @PathVariable Long optionId,
                                                          @Valid @RequestBody OptionRequestDTO optionRequestDTO) {
        OptionResponseDTO optionResponseDTO = optionService.modifyOption(productId, optionId, optionRequestDTO);
        return new ResponseEntity<>(optionResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}/options/{optionId}")
    @Operation(summary = "상품 옵션 삭제",
            description = "지정된 상품 옵션을 삭제합니다.",
            responses = {
                @ApiResponse(responseCode = "204", description = "상품 옵션 삭제 성공"),
                @ApiResponse(responseCode = "400", description = "잘못 된 값 입력"),
                @ApiResponse(responseCode = "401", description = "인증 오류"),
                @ApiResponse(responseCode = "404", description = "존재하지 않는 productId, optionId"),
                @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<Void> deleteOption(@LoginUser User user,
                                             @PathVariable Long productId,
                                             @PathVariable Long optionId) {
        optionService.deleteOption(productId, optionId);

        return ResponseEntity.noContent().build();
    }
}
