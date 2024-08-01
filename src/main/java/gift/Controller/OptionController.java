package gift.Controller;

import gift.DTO.OptionDTO;
import gift.Service.OptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/options")
public class OptionController {

    @Autowired
    private OptionService optionService;

    @PostMapping
    public ResponseEntity<OptionDTO> createOption(@RequestBody @Valid OptionDTO optionDTO) {
        OptionDTO createdOption = optionService.createOption(optionDTO);
        return ResponseEntity.ok(createdOption);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OptionDTO> getOptionById(@PathVariable Long id) {
        OptionDTO optionDTO = optionService.getOptionById(id);
        return ResponseEntity.ok(optionDTO);
    }

    @GetMapping
    public ResponseEntity<List<OptionDTO>> getAllOptions() {
        List<OptionDTO> options = optionService.getAllOptions();
        return ResponseEntity.ok(options);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OptionDTO> updateOption(@PathVariable Long id, @RequestBody @Valid OptionDTO optionDTO) {
        OptionDTO updatedOption = optionService.updateOption(id, optionDTO);
        return ResponseEntity.ok(updatedOption);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long id) {
        optionService.deleteOption(id);
        return ResponseEntity.noContent().build();
    }
}
