package gift.classes.RequestState;

import gift.dto.OptionDto;
import java.util.List;
import org.springframework.http.HttpStatus;

public class OptionListRequestStateDTO extends RequestStateDTO {

    private final List<OptionDto> data;

    public OptionListRequestStateDTO(HttpStatus status, String message,
        List<OptionDto> data) {
        super(status, message);
        this.data = data;
    }

    public List<OptionDto> getData() {
        return data;
    }
}
