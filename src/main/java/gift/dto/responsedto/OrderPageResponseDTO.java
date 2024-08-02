package gift.dto.responsedto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.dto.common.PageInfo;
import java.util.List;

public record OrderPageResponseDTO(
    PageInfo pageInfo,
    @JsonProperty("orderDtoList")
    List<OrderResponseDTO> orderResponseDTOList
) {
}