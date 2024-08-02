package gift.controller;

import gift.auth.CheckRole;
import gift.exception.InputException;
import gift.request.OptionsRequest;
import gift.response.option.ProductOptionsResponse;
import gift.service.OptionsService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OptionsApiController {

    private final OptionsService optionsService;
    private final ProductService productService;

    public OptionsApiController(OptionsService optionsService, ProductService productService) {
        this.optionsService = optionsService;
        this.productService = productService;
    }

    @CheckRole("ROLE_USER")
    @GetMapping("/api/products/{productId}/options")
    public ResponseEntity<ProductOptionsResponse> getProductWithAllOptions(
        @PathVariable("productId") Long id) {
        ProductOptionsResponse dto = optionsService.getAllProductOptions(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @CheckRole("ROLE_USER")
    @PostMapping("/api/products/{productId}/options")
    public ResponseEntity<Void> addOptions(@PathVariable("productId") Long id, @RequestBody @Valid
    OptionsRequest dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputException(bindingResult.getAllErrors());
        }

        optionsService.addOption(dto.optionName(), dto.quantity(), id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @CheckRole("ROLE_USER")
    @PutMapping("/api/products/{productId}/options/{optionId}")
    public ResponseEntity<Void> updateOptions(@PathVariable("productId") Long productId,
        @PathVariable("optionId") Long optionId,
        @RequestBody @Valid OptionsRequest dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputException(bindingResult.getAllErrors());
        }

        optionsService.updateOption(optionId, dto.optionName(), dto.quantity(), productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CheckRole("ROLE_USER")
    @DeleteMapping("/api/products/{productId}/options/{optionId}")
    public ResponseEntity<Void> deleteOptions(@PathVariable("productId") Long productId,
        @PathVariable("optionId") Long optionId) {
        optionsService.deleteOption(productId, optionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
