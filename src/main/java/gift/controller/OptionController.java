package gift.controller;

import gift.domain.Option.Option;
import gift.domain.Option.OptionRequest;
import gift.domain.Option.OptionResponse;
import gift.repository.MenuRepository;
import gift.service.OptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/products/{productId}/options")
public class OptionController {
    OptionService optionService;
    
    public OptionController(OptionService optionService,MenuRepository menuRepository){
        this.optionService = optionService;
    }
    
    @PostMapping
    public ResponseEntity<OptionResponse> save(
            @PathVariable("productId") Long productId,
            @RequestBody OptionRequest optionRequest
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(optionService.save(optionRequest,productId));
    }

    @PutMapping("/{optionId}")
    public ResponseEntity<Void> updateOption(
            @PathVariable("productId") Long productId,
            @PathVariable("optionId") Long optionId,
            @RequestBody OptionRequest optionRequest
    ){
        optionService.update(optionRequest,productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> deleteOption(
            @PathVariable("productId") Long productId,
            @PathVariable("optionId") Long optionId
    ){
        optionService.delete(optionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
