package gift.classes.RequestState;

import gift.dto.OptionDto;
import java.util.List;

public class OptionRequestStateDTO extends RequestStateDTO {

    private final List<OptionDto> options;

    public OptionRequestStateDTO(RequestStatus requestStatus, String details,
        List<OptionDto> options) {
        super(requestStatus, details);
        this.options = options;
    }

    public List<OptionDto> getOptions() {
        return options;
    }
}
