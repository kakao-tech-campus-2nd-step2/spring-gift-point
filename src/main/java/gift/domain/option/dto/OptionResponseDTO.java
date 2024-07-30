package gift.domain.option.dto;

import gift.domain.option.Option;
import java.util.ArrayList;
import java.util.List;

public class OptionResponseDTO {

    private List<SimpleOptionDTO> options = new ArrayList<>();

    public OptionResponseDTO(List<Option> optionList) {
        for (Option option : optionList) {
            options.add(new SimpleOptionDTO(option));
        }
    }

    public List<SimpleOptionDTO> getOptions() {
        return options;
    }
}