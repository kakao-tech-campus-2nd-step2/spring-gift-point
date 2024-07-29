package gift.controller;

import gift.dto.request.OptionRequest;
import gift.dto.response.OptionResponse;
import gift.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/options")
public class OptionRestController {
    public final OptionService optionService;

    public OptionRestController(OptionService optionService) {
        this.optionService = optionService;
    }

    @Operation(summary = "ID로 옵션을 조회합니다")
    @GetMapping("/{id}")
    public ResponseEntity<OptionResponse> getOptionById(@PathVariable Long id){
        OptionResponse foundOption = optionService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(foundOption);
    }

    @Operation(summary = "모든 옵션을 조회합니다")
    @GetMapping
    public ResponseEntity<List<OptionResponse>> getAllOptions(){
        return ResponseEntity.status(HttpStatus.OK).body(optionService.findAll());
    }

    @Operation(summary = "ID로 옵션을 삭제합니다")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOptionById(@PathVariable Long id){
        optionService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "ID로 옵션을 업데이트합니다")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateOptionById(@PathVariable Long id, @RequestBody OptionRequest optionRequest){
        optionService.updateById(id,optionRequest);
        return ResponseEntity.ok().build();
    }
}
