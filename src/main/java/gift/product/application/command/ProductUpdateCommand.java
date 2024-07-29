package gift.product.application.command;

import gift.category.domain.Category;
import gift.option.application.command.OptionUpdateCommand;
import gift.product.domain.Product;

import java.util.List;

public record ProductUpdateCommand(
        Long id,
        String name,
        Integer price,
        String imageUrl,
        Long categoryId,
        List<OptionUpdateCommand> optionUpdateCommandList
) {
    public Product toProduct(Category category) {
        return new Product(id, name, price, imageUrl, category);
    }
}
