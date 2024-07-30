package gift.main.dto;

import gift.main.Exception.CustomException;
import gift.main.Exception.ErrorCode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Validated
public record OptionListRequest(
        @Size(min = 1) @Valid List<OptionRequest> optionRequests) {

    public OptionListRequest {

        Set<OptionRequest> uniqueOptions = new HashSet<>();
        for (OptionRequest optionRequest : optionRequests) {
            if (!uniqueOptions.add(optionRequest)) {
                throw new CustomException(ErrorCode.DUPLICATE_OPTION);
            }
        }
    }

    public OptionListRequest(ProductAllRequest productAllRequest) {
        this(productAllRequest.optionRequests());
    }
}
