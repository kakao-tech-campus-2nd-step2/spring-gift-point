package gift.product.application.dto.request;

import gift.product.service.command.ProductCommand;
import gift.product.service.command.ProductOptionCommand;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ProductCreateRequest(
        @NotNull
        ProductRequest product,

        @NotNull
        List<ProductOptionRequest> options
) {
    public ProductCommand getProductCommand() {
        return product.toProductCommand();
    }

    public List<ProductOptionCommand> getProductOptionCommands() {
        return options.stream()
                .map(ProductOptionRequest::toProductOptionCommand)
                .toList();
    }
}
