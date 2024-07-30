package gift.controller.dto.response;

import gift.model.Option;

import java.util.List;

public class OptionResponse{

    public record InfoList(
            int optionCount,
            List<Info> options
    ) {
        public static InfoList from(List<Option> options) {
            return new InfoList(
                    options.size(),
                    options.stream().map(Info::from).toList()
            );
        }
    }

    public record Info(
        Long id,
        String name,
        int quantity
    ) {
        public static Info from(Option option) {
            return new Info(
                    option.getId(),
                    option.getName(),
                    option.getQuantity()
            );
        }
    }
}
