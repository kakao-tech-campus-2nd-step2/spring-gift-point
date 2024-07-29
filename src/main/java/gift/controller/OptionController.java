package gift.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gift.dto.OptionDto;
import gift.dto.response.OptionResponse;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.Map;
import java.util.HashMap;

@RestController
@Tag(name = "option", description = "Option API")
@RequestMapping("/api/products")
public class OptionController{

    private final OptionService optionService;

    public OptionController(OptionService optionService){
        this.optionService = optionService;
    }

    @GetMapping("/{id}/options")
    @Operation(summary = "옵션 조회", description = "productId 파라미터를 받아 상품에 옵션을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "옵션 조회 성공"),
        @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    public ResponseEntity<OptionResponse> getOptions(@PathVariable Long productId){
        OptionResponse optionResponse = optionService.findByProductId(productId);
        return new ResponseEntity<>(optionResponse, HttpStatus.OK);
    }

    @PostMapping("/{id}/options/new")
    @Operation(summary = "옵션 추가", description = "파라미터로 받은 옵션을 상품에 추가합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "옵션 생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 파라미터 요청"),
        @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음"),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 옵션"),
    })
    public ResponseEntity<?> addOption(@Valid @RequestBody OptionDto optionDto, BindingResult bindingResult, @PathVariable Long productId){
        
        if(bindingResult.hasErrors()){
            Map<String, String> erros = new HashMap<>();
            for(FieldError error : bindingResult.getFieldErrors()){
                erros.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(erros, HttpStatus.BAD_REQUEST);
        }
        optionService.addOption(optionDto, productId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
