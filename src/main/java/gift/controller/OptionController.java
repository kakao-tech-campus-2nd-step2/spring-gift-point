package gift.controller;

import gift.constants.SuccessMessage;
import gift.dto.OptionEditRequest;
import gift.dto.OptionSaveRequest;
import gift.service.OptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products/product/{productId}/options")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping
    public ResponseEntity<String> addOption(@RequestBody @Valid OptionSaveRequest saveRequest) {
        optionService.saveOption(saveRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(SuccessMessage.ADD_OPTION_SUCCESS_MSG);
    }

    @PutMapping
    public ResponseEntity<String> editOption(@RequestBody @Valid OptionEditRequest editRequest) {
        optionService.editOption(editRequest);
        return ResponseEntity.ok(SuccessMessage.EDIT_OPTION_SUCCESS_MSG);
    }

    @DeleteMapping("/{optionId}")
    public ResponseEntity<String> deleteOption(@PathVariable("productId") Long productId,
        @PathVariable("optionId") Long optionId) {
        optionService.deleteOption(productId, optionId);
        return ResponseEntity.ok(SuccessMessage.DELETE_OPTION_SUCCESS_MSG);
    }
}
