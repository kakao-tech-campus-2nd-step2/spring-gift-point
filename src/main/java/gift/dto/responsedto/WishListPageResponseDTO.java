package gift.dto.responsedto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.dto.common.PageInfo;
import java.util.List;

public record WishListPageResponseDTO(
    PageInfo pageInfo,
    @JsonProperty("wishDtoList")
    List<WishResponseDTO> wishListResponseDTO
) {
}