package gift.dto.option;

import gift.model.option.Option;

import java.util.List;

public class OptionResponse {
    public record Info(
            Long id,
            String name,
            int quantity
    ){
        public static Info fromEntity(Option option) {
            return new Info(option.getId(), option.getName(), option.getQuantity());
        }
    }
    public record InfoList(
            int optionCount,
            List<Info> options
    ){
        public static InfoList fromEntity(List<Option> options){
            return new InfoList(options.size(),options.stream().map(Info::fromEntity).toList());
        }
    }
}