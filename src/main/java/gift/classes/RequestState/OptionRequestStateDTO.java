package gift.classes.RequestState;

import gift.dto.OptionDto;
import org.springframework.http.HttpStatus;

public class OptionRequestStateDTO extends RequestStateDTO {

    private final OptionDto data;

    public OptionRequestStateDTO(HttpStatus status, String message, OptionDto data) {
        super(status, message);
        this.data = data;
    }

    public OptionDto getData() {
        return data;
    }
}
