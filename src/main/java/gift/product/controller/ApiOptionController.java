package gift.product.controller;

import static gift.product.exception.GlobalExceptionHandler.UNKNOWN_VALIDATION_ERROR;

import gift.product.docs.OptionControllerDocs;
import gift.product.dto.OptionDTO;
import gift.product.model.Option;
import gift.product.service.OptionService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products/{productId}/options")
public class ApiOptionController implements OptionControllerDocs {

    private final OptionService optionService;

    @Autowired
    public ApiOptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    public Page<Option> getAllOptions(
        @PathVariable Long productId,
        Pageable pageable) {
        System.out.println("[ApiOptionController] getAllOptions()");
        return optionService.getAllOptions(productId, pageable);
    }

    @PostMapping
    public ResponseEntity<Option> registerOption(
        @PathVariable Long productId,
        @Valid @RequestBody OptionDTO optionDTO,
        BindingResult bindingResult) {
        System.out.println("[ApiOptionController] registerOption()");
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null)
                throw new ValidationException(fieldError.getDefaultMessage());
            throw new ValidationException(UNKNOWN_VALIDATION_ERROR);
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(optionService.registerOption(productId, optionDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Option> updateOption(
        @PathVariable Long id,
        @PathVariable Long productId,
        @RequestBody OptionDTO optionDTO,
        BindingResult bindingResult) {
        System.out.println("[ApiOptionController] updateOption()");
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null)
                throw new ValidationException(fieldError.getDefaultMessage());
            throw new ValidationException(UNKNOWN_VALIDATION_ERROR);
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(optionService.updateOption(id, productId, optionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOption(
        @PathVariable Long id,
        @PathVariable Long productId) {
        System.out.println("[ApiOptionController] deleteOption()");
        optionService.deleteOption(id, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
