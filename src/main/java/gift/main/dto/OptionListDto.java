package gift.main.dto;

import gift.main.entity.Option;

import java.util.List;

public class OptionListDto {
    private final List<Option> options;

    public OptionListDto(List<Option> options) {
        this.options = options;
    }

    //중복 체크

}
