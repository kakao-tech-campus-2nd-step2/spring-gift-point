package gift.controller;

import gift.dto.ApiResponse;
import gift.model.Option;
import gift.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/products")
public class OptionController {

    private final OptionService optionService;

    @Autowired
    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping("/{productId}/options")
    public ResponseEntity<ApiResponse<Option>> addOptionToProduct(@PathVariable Long productId, @RequestBody Option option) {
        try {
            Option createdOption = optionService.addOptionToProduct(productId, option);
            ApiResponse<Option> response = new ApiResponse<>(true, "Option added successfully", createdOption, null);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            ApiResponse<Option> response = new ApiResponse<>(false, e.getMessage(), null, "400");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            ApiResponse<Option> response = new ApiResponse<>(false, e.getMessage(), null, "404");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            ApiResponse<Option> response = new ApiResponse<>(false, "Failed to add option: " + ex.getMessage(), null, "500");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{productId}/options")
    public ResponseEntity<ApiResponse<List<Option>>> getOptionsByProduct(@PathVariable Long productId) {
        try {
            List<Option> options = optionService.getOptionsByProduct(productId);
            ApiResponse<List<Option>> response = new ApiResponse<>(true, "Options retrieved successfully", options, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            ApiResponse<List<Option>> response = new ApiResponse<>(false, e.getMessage(), null, "404");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{productId}/options/{optionId}")
    public ResponseEntity<ApiResponse<Option>> updateOption(@PathVariable Long productId, @PathVariable Long optionId, @RequestBody Option option) {
        try {
            Option updatedOption = optionService.updateOption(productId, optionId, option);
            ApiResponse<Option> response = new ApiResponse<>(true, "Option updated successfully", updatedOption, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            ApiResponse<Option> response = new ApiResponse<>(false, e.getMessage(), null, "400");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            ApiResponse<Option> response = new ApiResponse<>(false, e.getMessage(), null, "404");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            ApiResponse<Option> response = new ApiResponse<>(false, "Failed to update option: " + ex.getMessage(), null, "500");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{productId}/options/{optionId}")
    public ResponseEntity<ApiResponse<Void>> deleteOption(@PathVariable Long productId, @PathVariable Long optionId) {
        try {
            optionService.deleteOption(productId, optionId);
            ApiResponse<Void> response = new ApiResponse<>(true, "Option deleted successfully", null, null);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            ApiResponse<Void> response = new ApiResponse<>(false, e.getMessage(), null, "404");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            ApiResponse<Void> response = new ApiResponse<>(false, "Failed to delete option: " + ex.getMessage(), null, "500");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
