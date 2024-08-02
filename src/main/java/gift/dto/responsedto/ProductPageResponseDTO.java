package gift.dto.responsedto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.dto.common.PageInfo;
import java.util.List;

public record ProductPageResponseDTO (
    PageInfo pageInfo,
    @JsonProperty("productDtoList")
    List<ProductResponseDTO> productResponseDTOList
) {
}