package gift.controller;

import gift.dto.ApiResponse;
import gift.model.Option;
import gift.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        Option createdOption = optionService.addOptionToProduct(productId, option);
        ApiResponse<Option> response = new ApiResponse<>(true, "Option added successfully", createdOption, null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{productId}/options")
    public ResponseEntity<ApiResponse<List<Option>>> getOptionsByProduct(@PathVariable Long productId) {
        List<Option> options = optionService.getOptionsByProduct(productId);
        ApiResponse<List<Option>> response = new ApiResponse<>(true, "Options retrieved successfully", options, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
