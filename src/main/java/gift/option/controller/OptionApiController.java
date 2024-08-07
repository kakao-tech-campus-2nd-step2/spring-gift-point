package gift.option.controller;

import static org.springframework.http.ResponseEntity.ok;

import gift.option.dto.OptionResponseDto;
import gift.option.service.OptionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class OptionApiController {

    private final OptionService optionService;

    @Autowired
    public OptionApiController(OptionService optionService) {
        this.optionService = optionService;
    }

    // 관리자 권한으로 존재하는 모든 옵션을 볼 수 있는 핸들러.
    @GetMapping("/admin/options")
    public ResponseEntity<List<OptionResponseDto>> readOptions() {
        return ok(optionService.selectOptions());
    }

    // 제품에 종속되는 옵션 조회 핸들러.
    // 제 코드라면 ProductController에 들어가야 할 것 같은데, 팀원들끼리 구현한 방법이 달라서 일단은 Option API에 넣었습니다.
    @GetMapping("products/{productId}/options")
    public ResponseEntity<List<OptionResponseDto>> readProductsOptions(
        @PathVariable long productId) {
        return ok(optionService.selectProductOptions(productId));
    }
}
