package gift.classes.RequestState;

import gift.dto.WishDto;
import org.springframework.http.HttpStatus;

public class WishListRequestStateDTO extends RequestStateDTO {

    private final WishDto data;

    public WishListRequestStateDTO(HttpStatus status, String message, WishDto data) {
        super(status, message);
        this.data = data;
    }

    public WishDto getData() {
        return data;
    }
}
