package gift.controller;

import gift.entity.Option;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/options")
@Tag(name = "Option rest API", description = "옵션 관련 rest API")
public class OptionRestController {

    private final OptionService optionService;

    @Autowired
    public OptionRestController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping
    @Operation(summary = "옵션 생성", description = "새로운 옵션을 생성합니다.")
    public Option createOption(@RequestBody Option option) {
        return optionService.saveOption(option);
    }

    @PutMapping("/{id}")
    @Operation(summary = "옵션 업데이트", description = "기존 옵션을 업데이트합니다.")
    public Option updateOption(@PathVariable Long id, @RequestBody Option optionDetails) {
        return optionService.updateOption(id, optionDetails);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "옵션 삭제", description = "ID로 특정 옵션을 삭제합니다.")
    public void deleteOption(@PathVariable Long id) {
        optionService.deleteOption(id);
    }
}
