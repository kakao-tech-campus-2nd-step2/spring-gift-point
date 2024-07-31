package gift.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gift.dto.request.OptionRequest;
import gift.dto.response.GetOptionsResponse;
import gift.dto.response.OptionResponse;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "option", description = "Option API")
@RequestMapping("/api/products")
public class OptionController{

    private final OptionService optionService;

    public OptionController(OptionService optionService){
        this.optionService = optionService;
    }

    @GetMapping("/{productId}/options")
    @Operation(summary = "옵션 조회", description = "productId 파라미터를 받아 상품에 옵션을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "옵션 조회 성공"),
        @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    public ResponseEntity<GetOptionsResponse> getOptions(@PathVariable Long productId){
        return new ResponseEntity<>(optionService.findByProductId(productId), HttpStatus.OK);
    }

    @PostMapping("/{productId}/options/new")
    @Operation(summary = "옵션 추가", description = "파라미터로 받은 옵션을 상품에 추가합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "옵션 생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 파라미터 요청"),
        @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음"),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 옵션"),
    })
    public ResponseEntity<OptionResponse> addOption(@Valid @RequestBody OptionRequest optionRequest, BindingResult bindingResult, @PathVariable Long productId){
        return new ResponseEntity<>(optionService.addOption(optionRequest, productId), HttpStatus.CREATED);
    }

    @PutMapping("{productId}/options/{optionId}")
    @Operation(summary = "옵션 수정", description = "옵션의 정보를 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "옵션 수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 파라미터 요청"),
        @ApiResponse(responseCode = "404", description = "상품혹은 옵션을 찾을 수 없음"),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 옵션"),
    })
    public ResponseEntity<Void> updateOption(@Valid @RequestBody OptionRequest optionRequest, BindingResult bindingResult, @PathVariable Long productId, @PathVariable Long optionId){
        optionService.updateOption(optionRequest, productId, optionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{productId}/options/{optionId}")
    @Operation(summary = "옵션 삭제", description = "옵션의 정보를 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "옵션 수정 성공"),
        @ApiResponse(responseCode = "400", description = "상품의 옵션은 최소 1개는 존재해야함"),
        @ApiResponse(responseCode = "404", description = "상품혹은 옵션을 찾을 수 없음"),
    })
    public ResponseEntity<Void> deleteOption(@PathVariable Long productId, @PathVariable Long optionId){
        optionService.deleteOption(productId, optionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
