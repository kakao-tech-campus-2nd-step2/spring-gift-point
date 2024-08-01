package gift.controller;

import gift.dto.ApiResponse;
import gift.dto.OptionRequest;
import gift.model.HttpResult;
import gift.service.OptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping("/option/add")
    public ResponseEntity<ApiResponse> addOptions(@RequestBody OptionRequest optionRequest) {
        var option = optionService.createOption(optionRequest);
        var addOptionSucessResponse = new ApiResponse(HttpResult.OK, option.toString(),
            HttpStatus.OK);
        return new ResponseEntity<>(addOptionSucessResponse,
            addOptionSucessResponse.getHttpStatus());
    }

    @GetMapping("/options")
    public ResponseEntity<ApiResponse> getAllOptions() {
        var optionList = optionService.retreiveOptions();
        var apiResponse = new ApiResponse(HttpResult.OK, optionList.toString(), HttpStatus.OK);
        return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatus());
    }

    @GetMapping("/options/{id}")
    public ResponseEntity<ApiResponse> getOptionById(@PathVariable("id") Long id) {
        var option = optionService.retrieveOption(id);
        var apiResponse = new ApiResponse(HttpResult.OK, option.toString(), HttpStatus.OK);
        return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatus());
    }

    @DeleteMapping("/option/delete")
    public ResponseEntity<ApiResponse> deleteOptionById(@RequestParam(name = "id") Long id) {
        optionService.deleteOptions(id);
        var apiResponse = new ApiResponse(HttpResult.OK, "삭제 성공", HttpStatus.OK);
        return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatus());
    }

    @PutMapping("/option/update")
    public ResponseEntity<ApiResponse> updateOption(@RequestBody OptionRequest optionRequest) {
        var option = optionService.updateOption(optionRequest);
        var apiResponse = new ApiResponse(HttpResult.OK, option.toString(), HttpStatus.OK);
        return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatus());
    }
}
