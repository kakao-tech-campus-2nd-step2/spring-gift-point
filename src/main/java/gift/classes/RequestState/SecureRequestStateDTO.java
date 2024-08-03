package gift.classes.RequestState;

import gift.dto.TokenDto;
import org.springframework.http.HttpStatus;

public class SecureRequestStateDTO extends RequestStateDTO {

    public TokenDto data;

    public SecureRequestStateDTO(HttpStatus status, String message, TokenDto data) {
        super(status, message);
        this.data = data;
    }

    public TokenDto getData() {
        return data;
    }
}