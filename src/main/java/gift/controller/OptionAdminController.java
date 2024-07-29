package gift.controller;

import gift.controller.dto.OptionRequest;
import gift.controller.dto.OptionResponse;
import gift.service.OptionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/products/options")
public class OptionAdminController {
    OptionService optionService;

    public OptionAdminController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping
    public ResponseEntity<OptionResponse> createOption(@Valid @RequestBody OptionRequest optionRequest){
        OptionResponse optionResponse = optionService.addOption(optionRequest);
        return ResponseEntity.ok(optionResponse);
    }

    @DeleteMapping
    public ResponseEntity<Long> delete(@Valid @RequestBody Long id){
        Long deleteId = optionService.deleteOption(id);
        return ResponseEntity.ok(deleteId);
    }
}
