package gift.classes.RequestState;

import gift.dto.WishDto;
import org.springframework.data.domain.Page;

public class WishListRequestStateDTO extends RequestStateDTO {

    private final Page<WishDto> wishes;

    public WishListRequestStateDTO(RequestStatus requestStatus, String details,
        Page<WishDto> wishes) {
        super(requestStatus, details);
        this.wishes = wishes;
    }

    public Page<WishDto> getWishes() {
        return wishes;
    }
}
