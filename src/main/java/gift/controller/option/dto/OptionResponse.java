package gift.controller.option.dto;

import gift.model.Option;
import java.util.List;

public class OptionResponse {

    public record Info(
        Long id,
        String name,
        Integer quantity
    ) {

        public static Info from(Option option) {
            return new Info(option.getId(), option.getName(), option.getQuantity());
        }
    }

    public record InfoList(
        int optionCount,
        List<Info> options
    ) {

        public static InfoList from(List<Option> options) {
            List<Info> responses = options.stream()
                .map(Info::from)
                .toList();
            return new InfoList(responses.size(), responses);
        }
    }

}
