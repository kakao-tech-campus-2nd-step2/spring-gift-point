package gift.controller;

import gift.dto.DeleteOptionRequest;
import gift.dto.OptionRequest;
import gift.dto.OptionResponse;
import gift.entity.Option;
import gift.service.OptionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/products/{productId}/options")
@Tag(name = "Product Option Management System", description = "Operations related to product options")
public class OptionController {
    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    @Operation(summary = "Get options for a product", description = "Fetches all options for a specified product", tags = { "Product Option Management System" })
    public ResponseEntity<List<OptionResponse>> getOptions(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable Long productId) {
        List<OptionResponse> options = optionService.getOptionsByProductId(productId);
        return ResponseEntity.ok(options);
    }

    @PostMapping
    @Operation(summary = "Add an option to a product", description = "Adds a new option to a specified product", tags = { "Product Option Management System" })
    public ResponseEntity<OptionResponse> addOptionToProduct(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable Long productId,
            @Parameter(description = "Option details", required = true)
            @Valid @RequestBody OptionRequest optionRequest,
            UriComponentsBuilder uriBuilder) {
        Option option = optionService.addOptionToProduct(productId, optionRequest);
        OptionResponse response = new OptionResponse(option.getId(), option.getName(), option.getQuantity());
        URI location = uriBuilder.path("/api/products/{productId}/options/{optionId}").buildAndExpand(productId, option.getId()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{optionId}")
    @Operation(summary = "Update an option for a product", description = "Updates an existing option of a specified product", tags = { "Product Option Management System" })
    public ResponseEntity<OptionResponse> updateOption(
            @Parameter(description = "ID of the option to be updated", required = true)
            @PathVariable Long optionId,
            @Parameter(description = "Updated option details", required = true)
            @RequestBody OptionRequest optionRequest,
            @Parameter(description = "ID of the product", required = true)
            @PathVariable String productId) {
        OptionResponse updatedOption = optionService.updateOption(optionId, optionRequest.getName(), optionRequest.getQuantity());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{optionId}")
    @Operation(summary = "Delete an option from a product", description = "Deletes an option from a specified product", tags = { "Product Option Management System" })
    public ResponseEntity<Void> deleteOption(
            @Parameter(description = "ID of the option to be deleted", required = true)
            @PathVariable Long optionId,
            @Parameter(description = "ID of the product", required = true)
            @PathVariable String productId) {
        optionService.deleteOption(optionId, DeleteOptionRequest.getEmail(), DeleteOptionRequest.getPassword());
        return ResponseEntity.noContent().build();
    }
}
