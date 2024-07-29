package gift.Controller;

import gift.Model.DTO.OptionDTO;
import gift.Service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "옵션 API", description = "옵션과 관련된 API")
@RestController
@RequestMapping("/api")
public class OptionRestController {
    private final OptionService optionService;

    public OptionRestController(OptionService optionService){
        this.optionService = optionService;
    }

    @Operation(summary = "옵션 조회", description = "상품의 id를 받으며, 해당 상품의 모든 옵션을 조회한다.")
    @Parameter(name="id", description = "상품의 id로, 해당 상품의 옵션을 조회하는데 사용된다.")
    @GetMapping("/products/{id}/options")
    public List<OptionDTO> read(@PathVariable Long id){
        return optionService.read(id);
    }
}
