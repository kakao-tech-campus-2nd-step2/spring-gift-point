package gift.controller;

import gift.dto.OptionDTO;
import gift.dto.PageRequestDTO;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/options")
@Validated
@Tag(name = "Option Management", description = "APIs for managing options")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    @Operation(summary = "Get all options", description = "This API retrieves all options with pagination.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved options."),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public String allOptions(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(30) int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "asc") String direction,
        Model model) {

        PageRequestDTO pageRequestDTO = new PageRequestDTO(page, size, sortBy, direction);
        Page<OptionDTO> optionPage = optionService.findAllOptions(pageRequestDTO);

        model.addAttribute("options", optionPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", optionPage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);

        return "Options";
    }

    @GetMapping("/add")
    @Operation(summary = "Get add option form", description = "This API returns the form to add a new option.")
    public String addOptionForm(Model model) {
        model.addAttribute("option", new OptionDTO());
        return "Add_option";
    }

    @PostMapping
    @Operation(summary = "Add a new option", description = "This API adds a new option.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successfully added option."),
        @ApiResponse(responseCode = "400", description = "Invalid option data."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public String addOption(@Valid @ModelAttribute("option") OptionDTO optionDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "Add_option";
        }
        optionService.addOption(optionDTO);
        return "redirect:/admin/options";
    }

    @GetMapping("/edit/{id}")
    @Operation(summary = "Get edit option form", description = "This API returns the form to edit an existing option.")
    public String editOptionForm(@PathVariable Long id, Model model) {
        Optional<OptionDTO> optionDTO = optionService.findOptionById(id);
        if (optionDTO.isEmpty()) {
            return "redirect:/admin/options";
        }
        model.addAttribute("option", optionDTO.get());
        return "Edit_option";
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing option", description = "This API updates an existing option.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated option."),
        @ApiResponse(responseCode = "400", description = "Invalid option data."),
        @ApiResponse(responseCode = "404", description = "Option not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public String updateOption(@Valid @ModelAttribute("option") OptionDTO optionDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "Edit_option";
        }
        optionService.updateOption(optionDTO);
        return "redirect:/admin/options";
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an option", description = "This API deletes an existing option.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted option."),
        @ApiResponse(responseCode = "404", description = "Option not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public String deleteOption(@PathVariable Long id) {
        optionService.deleteOption(id);
        return "redirect:/admin/options";
    }
}