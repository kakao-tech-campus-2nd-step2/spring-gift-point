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

    // 관리자 권한으로 존재하는 모든 옵션을 볼 수 있는 핸들러.
    @GetMapping
    public ApiResponseDto<List<OptionResponseDto>> readOptions() {
        return SUCCESS(optionService.selectOptions());
    }
}
