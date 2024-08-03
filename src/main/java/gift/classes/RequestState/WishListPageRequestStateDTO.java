package gift.classes.RequestState;

import gift.dto.WishPageDto;
import org.springframework.http.HttpStatus;

public class WishListPageRequestStateDTO extends RequestStateDTO {

    private final WishPageDto data;

    public WishListPageRequestStateDTO(HttpStatus status, String message,
        WishPageDto data) {
        super(status, message);
        this.data = data;
    }

    public WishPageDto getData() {
        return data;
    }
}
