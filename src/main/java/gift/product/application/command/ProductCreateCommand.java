package gift.product.application.command;

import gift.category.domain.Category;
import gift.option.application.command.OptionCreateCommand;
import gift.product.domain.Product;

import java.util.List;

public record ProductCreateCommand(
        String name,
        Integer price,
        String imageUrl,
        Long categoryId,
        List<OptionCreateCommand> optionCreateCommandList
) {
    public Product toProduct(Category category) {
        return new Product(name, price, imageUrl, category);
    }
}
