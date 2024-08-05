package gift.Controller;

import gift.DTO.OptionDTO;
import gift.Service.OptionService;
import gift.util.CustomPageResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products/{productId}/options")
@Tag(name = "상품 옵션 관련 서비스", description = " ")
public class OptionController {

    @Autowired
    private OptionService optionService;

//    @Operation(summary = "상품 옵션 생성", description = "상품 옵션의 이름은 중복될 수 없습니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully created option"),
//            @ApiResponse(responseCode = "400", description = "Invalid input data")
//    })
//    @PostMapping
//    public ResponseEntity<OptionDTO> createOption(@RequestBody @Valid OptionDTO optionDTO) {
//        OptionDTO createdOption = optionService.createOption(optionDTO);
//        return ResponseEntity.ok(createdOption);
//    }

//    @Operation(summary = "해당 상품의 특정 옵션을 조회", description = "상품id, 상품명, 상품 가격, 옵션id, 옵션명, 재고, 생성일, 수정일을 반환합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully retrieved option"),
//            @ApiResponse(responseCode = "404", description = "Option not found")
//    })
//    @GetMapping("/{id}")
//    public ResponseEntity<OptionDTO> getOptionById(@PathVariable Long id) {
//        OptionDTO optionDTO = optionService.getOptionById(id);
//        return ResponseEntity.ok(optionDTO);
//    }

    @Operation(summary = "해당 상품 옵션 리스트 조회", description = "상품id, 옵션id, 옵션명, 재고를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of options")
    })
    @GetMapping
    public CustomPageResponse<OptionDTO> getOptionsByProductId(@PathVariable Long productId) {
        List<OptionDTO> resultData = optionService.getOptionsByProductId(productId).stream()
                .map(option -> new OptionDTO(
                        option.getProduct().getId(),
                        option.getId(),
                        option.getQuantity(),
                        option.getName()
                ))
                .collect(Collectors.toList());

        return new CustomPageResponse<>(
                resultData,
                0,
                1,
                false,
                resultData.size()
        );
    }



//    @GetMapping
//    public ResponseEntity<List<OptionDTO>> getAllOptions() {
//        List<OptionDTO> options = optionService.getAllOptions();
//        return ResponseEntity.ok(options);
//    }

//    @Operation(summary = "상품 옵션 수정", description = "상품 옵션의 이름은 중복될 수 없습니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully updated option"),
//            @ApiResponse(responseCode = "404", description = "Option not found"),
//            @ApiResponse(responseCode = "400", description = "Invalid input data")
//    })
//    @PutMapping("/{id}")
//    public ResponseEntity<OptionDTO> updateOption(@PathVariable Long id, @RequestBody @Valid OptionDTO optionDTO) {
//        OptionDTO updatedOption = optionService.updateOption(id, optionDTO);
//        return ResponseEntity.ok(updatedOption);
//    }
//
//    @Operation(summary = "상품 옵션 삭제", description = "상품은 최소 한개 이상의 옵션을 가져야 합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "204", description = "Successfully deleted option"),
//            @ApiResponse(responseCode = "404", description = "Option not found")
//    })
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteOption(@PathVariable Long id) {
//        optionService.deleteOption(id);
//        return ResponseEntity.noContent().build();
//    }
}
