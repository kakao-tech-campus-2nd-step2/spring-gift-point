package gift.option;

import gift.option.dto.OptionReqDto;
import gift.option.entity.Option;

public class OptionFixture {

    public static Option createOption(String name, Integer quantity) {
        return new Option(name, quantity);
    }

    public static OptionReqDto createOptionReqDto(String optionName, Integer quantity) {
        return new OptionReqDto(optionName, quantity);
    }
}
