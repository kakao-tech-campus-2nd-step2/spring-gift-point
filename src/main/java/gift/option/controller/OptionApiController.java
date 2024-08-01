package gift.option.controller;

import static gift.global.dto.ApiResponseDto.SUCCESS;

import gift.global.dto.ApiResponseDto;
import gift.option.dto.OptionRequestDto;
import gift.option.dto.OptionResponseDto;
import gift.option.service.OptionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/options")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class OptionApiController {

    private final OptionService optionService;

    @Autowired
    public OptionApiController(OptionService optionService) {
        this.optionService = optionService;
    }

    // 관리자 권한으로 옵션을 추가하는 핸들러.
    // 제품에 종속적이므로 productId가 필요하나, 연관관계 매핑을 하지 않아 존재하지 않는 id를 가져올 수도 있습니다.
    // 이미 존재하는 id를 가져오는 검증이 필요하기 때문에 resolver를 사용하여 존재하는 id만 가져오도록 합니다.
    @PostMapping
    public ApiResponseDto<Void> createOption(@RequestBody @Valid OptionRequestDto optionRequestDto,
        @RequestParam(name = "product-id") long productId) {
        optionService.insertOption(optionRequestDto, productId);

        return SUCCESS();
    }

    // 관리자 권한으로 존재하는 모든 옵션을 볼 수 있는 핸들러.
    @GetMapping
    public ApiResponseDto<List<OptionResponseDto>> readOptions() {
        return SUCCESS(optionService.selectOptions());
    }
}
