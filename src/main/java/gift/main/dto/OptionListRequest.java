package gift.main.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.main.Exception.CustomException;
import gift.main.Exception.ErrorCode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Validated
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record OptionListRequest(@Size(min = 1)
                                @Valid
                                List<OptionRequest> options) {

    public OptionListRequest {

        Set<OptionRequest> uniqueOptions = new HashSet<>();
        for (OptionRequest optionRequest : options) {
            if (!uniqueOptions.add(optionRequest)) {
                throw new CustomException(ErrorCode.DUPLICATE_OPTION);
            }
        }
    }

    public OptionListRequest(ProductAllRequest productAllRequest) {
        this(productAllRequest.options());
    }
}
