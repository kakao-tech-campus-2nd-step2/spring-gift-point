package gift.domain.controller;

import gift.domain.controller.apiResponse.OptionApiResponse;
import gift.domain.controller.apiResponse.OptionListApiResponse;
import gift.domain.dto.response.OptionDetailedResponse;
import gift.domain.service.OptionService;
import gift.global.apiResponse.SuccessApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/options")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    public ResponseEntity<OptionListApiResponse> getOptions() {
        return SuccessApiResponse.ok(new OptionListApiResponse(HttpStatus.OK, optionService.getAllOptions()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OptionApiResponse> getOption(@PathVariable("id") Long id) {
        return SuccessApiResponse.ok(new OptionApiResponse(HttpStatus.OK,
            OptionDetailedResponse.of(optionService.getOptionById(id))));
    }
}
