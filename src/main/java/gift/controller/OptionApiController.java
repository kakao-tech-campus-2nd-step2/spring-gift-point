package gift.controller;

import gift.dto.OptionQuantityRequestDto;
import gift.dto.OptionRequestDto;
import gift.dto.OptionResponseDto;
import gift.dto.ProductOptionRequestDto;
import gift.service.OptionService;
import gift.service.ProductService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class OptionApiController {

    private final OptionService optionService;
    private final ProductService productService;

    @Autowired
    public OptionApiController(OptionService optionService,ProductService productService){
        this.optionService = optionService;
        this.productService = productService;
    }
    // 상품 생성
    @PostMapping
    public ResponseEntity<Void> addProduct(@RequestBody ProductOptionRequestDto productOptionRequestDto){
        productService.save(productOptionRequestDto.getProductRequestDto(),productOptionRequestDto.getOptionRequestDto());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 옵션 추가
    @PostMapping("/{productId}/options")
    public ResponseEntity<Void> addOption(@PathVariable Long productId,
        @RequestBody OptionRequestDto optionRequestDto) {
        optionService.addOption(productId, optionRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    // 옵션 확인
    @GetMapping("/{productId}/options")
    public ResponseEntity<List<OptionResponseDto>> findAllOptions(@PathVariable Long productId) {
        List<OptionResponseDto> options = optionService.findAll(productId);
        return ResponseEntity.status(HttpStatus.OK).body(options);
    }

    // 옵션 삭제
    @DeleteMapping("/{productId}/options/{optionId}")
    public ResponseEntity<Void> deleteOptions(@PathVariable Long productId, @PathVariable Long optionId){
        optionService.deleteById(productId,optionId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 옵션의 수량 차감
    @PutMapping("/{productId}/options/{optionId}")
    public ResponseEntity<Void> subQuantity(@PathVariable Long productId,@PathVariable Long optionId ,@RequestBody OptionQuantityRequestDto optionQuantityRequestDto){
        optionService.updateOptionQuantity(productId,optionId,optionQuantityRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
