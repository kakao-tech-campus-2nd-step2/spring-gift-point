package gift.option;

import gift.exception.NotFoundOption;
import java.util.List;
import org.springframework.http.HttpEntity;
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
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping("/{productId}/options")
    public OptionResponseDto addOption(@PathVariable Long productId, @RequestBody OptionRequestDto optionRequestDto) {
        return optionService.postOption(optionRequestDto, productId);
    }

    @GetMapping("/{productId}/options")
    public List<OptionResponseDto> getOptionsInProduct(@PathVariable Long productId) {
        return optionService.findOption(productId);
    }

    @PutMapping("/options/{optionId}")
    public OptionResponseDto putOption(@PathVariable Long optionId, @RequestBody OptionRequestDto optionRequestDto)
        throws NotFoundOption {
        return optionService.updateOption(optionId, optionRequestDto);
    }

    @DeleteMapping("/options/{optionId}")
    public HttpEntity<String> deleteOptionById(@PathVariable Long optionId) throws NotFoundOption {
        optionService.deleteOptionById(optionId);
        return ResponseEntity.ok("성공적으로 삭제되었습니다");
    }

}
