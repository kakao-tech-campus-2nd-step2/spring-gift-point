package gift.controller;


import gift.dto.OptionDTO;

import gift.dto.OptionResponseDTO;
import gift.entity.Option;
import gift.entity.Product;
import gift.service.OptionFacadeService;
import gift.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/options")
@Tag(name = "Option(상품 옵션)", description = "상품 옵션 관련 API입니다.")
public class OptionController {

    private final OptionFacadeService optionService;

    public OptionController(OptionFacadeService optionService) {
        this.optionService = optionService;
    }


    @GetMapping()
    @Operation(summary = "ID로 Product 옵션 조회", description = "Product의 Id로 상품의 옵션을 가져옵니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "옵션 목록 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = OptionResponseDTO.class)))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 옵션", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class))))
    })

    public ResponseEntity<List<OptionResponseDTO>> getProductByIdWithOption(
        @Parameter(name = "productId", description = "Product Id", example = "1") @PathVariable("productId") long productId) {
        List<Option> options = optionService.getAllProductOption(productId);

        List<OptionResponseDTO> response = options.stream()
            .map(OptionResponseDTO::new)
            .collect(Collectors.toList());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "상품 Option 추가", description = "상품의 옵션을 추가합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "옵션 추가 성공"),
        @ApiResponse(responseCode = "400", description = "입력 데이터 잘못됨.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 상품.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
        @ApiResponse(responseCode = "409", description = "옵션명 중복", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class))))})
    @PostMapping()
    public ResponseEntity<String> addOption(
        @PathVariable("productId") Long productId, @RequestBody OptionDTO optionDTO) {
        Product product = optionService.findProductById(productId);
        Option option = optionDTO.toEntity(product);
        optionService.addOption(option);

        return new ResponseEntity<>("Option 추가 완료", HttpStatus.CREATED);
    }

    @PutMapping("/{optionId}")
    @Operation(summary = "상품 Option 수정", description = "id값에 해당하는 상품의 옵션을 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "옵션 수정 성공"),
        @ApiResponse(responseCode = "400", description = "입력 데이터 잘못됨.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
        @ApiResponse(responseCode = "404", description = "수정하려는 옵션 조회 실패.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
        @ApiResponse(responseCode = "409", description = "옵션 이름 중복 ", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class))))})
    public ResponseEntity<String> updateOption(@PathVariable("productId") Long productId,
        @PathVariable("optionId") Long optionId,
        @RequestBody OptionDTO optionDTO) {
        Product product = optionService.findProductById(productId);
        Option option = optionDTO.toEntity(product);
        optionService.updateOption(option, optionId);
        return new ResponseEntity<>("Option 수정 완료", HttpStatus.OK);
    }

    @DeleteMapping("/{optionId}")
    @Operation(summary = "상품 Option 삭제", description = "id값에 해당하는 상품의 옵션을 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "옵션 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "삭제시 상품의 옵션이 존재하지 않을때", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
        @ApiResponse(responseCode = "404", description = "삭제하려는 옵션 조회 실패.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class))))})

    public ResponseEntity<String> deleteOption(@PathVariable("productId") Long productId,
        @PathVariable("optionId") Long optionId) {
        optionService.deleteOption(optionId);
        return new ResponseEntity<>("Option 삭제 완료", HttpStatus.NO_CONTENT);
    }


}
