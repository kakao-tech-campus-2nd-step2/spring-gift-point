package gift.controller;

import gift.entity.Option;
import gift.service.OptionService;
import gift.domain.OptionDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.NoSuchElementException;

@Tag(name = "옵션", description = "상품 옵션 관련 API")
@RestController
@RequestMapping("/api/products/{product_id}/options")
public class OptionController {
    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    public ResponseEntity<List<Option>> getOption(@PathVariable int product_id) {
        var options = optionService.findByProduct_Id(product_id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 상품입니다.")).getOptions();
        return ResponseEntity.ok(options);
    }

    @PostMapping
    public ResponseEntity<Option> createOption(@PathVariable int product_id, @RequestBody OptionDTO optionDTO) throws URISyntaxException {
        var option = optionService.addOption(product_id, optionDTO);
        URI uri = new URI("/api/products/" + product_id + "/options/" + option.getId());
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Option> updateOption(@PathVariable("product_id") int product_id, @PathVariable("id") int id, @RequestBody OptionDTO optionDTO) {
        var option = optionService.updateOption(product_id, id, optionDTO);
        return ResponseEntity.ok(option);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOption(@PathVariable("product_id") int product_id, @PathVariable("id") int id) {
        optionService.deleteOption(product_id, id);
        return ResponseEntity.noContent().build();
    }
}
