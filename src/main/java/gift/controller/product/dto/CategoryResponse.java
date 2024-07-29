package gift.controller.product.dto;

import gift.application.product.dto.CategoryModel;
import java.util.List;

public class CategoryResponse {

    public record Info(
        Long id,
        String name,
        String imageUrl,
        String description,
        String color
    ) {

        public static Info from(CategoryModel.Info category) {
            return new Info(category.id(), category.name(), category.imageUrl(),
                category.description(), category.color());
        }
    }

    public record InfoList(
        List<Info> categories
    ) {

        public static InfoList from(List<CategoryModel.Info> categories) {
            return new InfoList(categories.stream().map(Info::from).toList());
        }
    }
}
