package gift.controller;

import gift.annotation.LoginUser;
import gift.dto.OptionDTO;
import gift.dto.OrderRequestDTO;
import gift.dto.OrderResponseDTO;
import gift.entity.Option;
import gift.entity.Product;
import gift.service.OptionFacadeService;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/option")
@Tag(name = "Option(상품 옵션)", description = "상품 옵션 관련 API입니다.")
public class OptionController {

    private final OptionFacadeService optionService;

    public OptionController(OptionFacadeService optionService) {
        this.optionService = optionService;
    }

    @Operation(summary = "상품 Option 추가", description = "상품의 옵션을 추가합니다.")
    @PostMapping
    public ResponseEntity<String> addOption(@RequestBody OptionDTO optionDTO) {
        Product product = optionService.findProductById(optionDTO.getProductId());
        Option option = optionDTO.toEntity(product);
        optionService.addOption(option);

        return new ResponseEntity<>("Option 추가 완료", HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "상품 Option 수정", description = "id값에 해당하는 상품의 옵션을 수정합니다.")
    public ResponseEntity<String> updateOption(@PathVariable("id") Long id,
        @RequestBody OptionDTO optionDTO) {
        Product product = optionService.findProductById(optionDTO.getProductId());
        Option option = optionDTO.toEntity(product);

        optionService.updateOption(option, id);
        return new ResponseEntity<>("Option 수정 완료", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "상품 Option 삭제", description = "id값에 해당하는 상품의 옵션을 삭제합니다.")
    public ResponseEntity<String> deleteOption(@PathVariable("id") Long id) {
        optionService.deleteOption(id);
        return new ResponseEntity<>("Option 삭제 완료", HttpStatus.NO_CONTENT);
    }


    //상품 주문
    @PostMapping("/orders")
    @Operation(summary = "상품 주문", description = "Option에 해당하는 상품을 주문합니다.")
    public ResponseEntity<OrderResponseDTO> orderOption(
        @RequestBody @Valid OrderRequestDTO orderRequestDTO,
        @LoginUser String email) {
        OrderResponseDTO response = optionService.orderOption(orderRequestDTO, email);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
