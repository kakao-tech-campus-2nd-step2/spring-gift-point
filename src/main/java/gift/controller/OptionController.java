package gift.controller;

import gift.entity.Option;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/options")
@Tag(name = "Option API", description = "옵션 관련 API")
public class OptionController {

    @Autowired
    private OptionService optionService;

    @GetMapping
    @Operation(summary = "모든 옵션 조회", description = "모든 옵션을 조회합니다.")
    public Set<Option> getAllOptions() {
        return optionService.getAllOptions();
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID로 옵션 조회", description = "ID로 특정 옵션을 조회합니다.")
    public ResponseEntity<Option> getOptionById(@PathVariable Long id) {
        try {
            Option option = optionService.getOptionById(id);
            return ResponseEntity.ok(option);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
