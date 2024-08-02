package gift.web.controller;

import gift.service.option.OptionService;
import gift.web.dto.MemberDto;
import gift.web.dto.option.OptionRequestDto;
import gift.web.dto.option.OptionResponseDto;
import gift.web.jwt.AuthUser;
import jakarta.validation.Valid;
import java.util.List;
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
public class OptionController {
    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping("/{productId}/options")
    public ResponseEntity<OptionResponseDto> createOption(@AuthUser MemberDto memberDto, @PathVariable Long productId, @RequestBody @Valid OptionRequestDto optionRequestDto) {
        return new ResponseEntity<>(optionService.createOption(productId, optionRequestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{productId}/options")
    public ResponseEntity<List<OptionResponseDto>> getOptionsByProductId(@PathVariable Long productId) {
        return new ResponseEntity<>(optionService.getOptionsByProductId(productId), HttpStatus.OK);
    }

    @PutMapping("/{productId}/options/{optionId}")
    public ResponseEntity<OptionResponseDto> updateOption(@AuthUser MemberDto memberDto, @PathVariable Long productId, @PathVariable Long optionId, @RequestBody @Valid OptionRequestDto optionRequestDto) {
        return new ResponseEntity<>(optionService.updateOption(optionId, productId,
            optionRequestDto), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}/options/{optionId}")
    public ResponseEntity<String> deleteOption(@AuthUser MemberDto memberDto, @PathVariable Long productId, @PathVariable Long optionId) {
        optionService.deleteOption(optionId, productId);
        return ResponseEntity.noContent().build();
    }
}
