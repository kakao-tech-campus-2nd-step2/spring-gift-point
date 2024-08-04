package gift.option.presentation;

import gift.option.presentation.request.OptionSubtractQuantityRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "옵션 API")
@RequestMapping("/api/options")
public interface OptionApi {

    @Operation(summary = "옵션 수량 감소")
    @PostMapping("/{id}/subtract")
    void subtractOptionQuantity(
            @Parameter(description = "옵션 ID", in = ParameterIn.PATH, required = true)
            @PathVariable("id") Long id,
            @Parameter(description = "옵션 수량 감소 요청 정보", required = true)
            @RequestBody OptionSubtractQuantityRequest request
    );
}
