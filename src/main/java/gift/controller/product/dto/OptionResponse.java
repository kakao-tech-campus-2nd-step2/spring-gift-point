package gift.controller.product.dto;

import gift.application.product.dto.OptionModel;
import java.util.List;

public class OptionResponse {

    public record Info(
        Long id,
        String name,
        Integer quantity
    ) {

        public static Info from(OptionModel.Info model) {
            return new Info(model.id(), model.name(), model.quantity());
        }
    }

    public record InfoList(
        Integer optionCount,
        List<Info> options
    ) {

        public static InfoList from(List<OptionModel.Info> models) {
            var options = models.stream()
                .map(Info::from)
                .toList();
            return new InfoList(models.size(), options);
        }
    }

}
