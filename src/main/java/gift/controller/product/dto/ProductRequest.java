package gift.controller.product.dto;


import gift.controller.product.dto.OptionRequest.Info;
import gift.application.product.dto.OptionCommand.RegisterMany;
import gift.application.product.dto.ProductCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class ProductRequest {

    public record Register(
        @NotBlank
        String name,
        @Min(0)
        Integer price,
        @NotBlank
        String imageUrl,
        @NotNull
        Long categoryId,
        @NotNull
        @Valid
        List<Info> options
    ) {

        public ProductCommand.Register toProductCommand() {
            return new ProductCommand.Register(name, price, imageUrl, categoryId);
        }

        public RegisterMany toOptionCommand() {
            return new RegisterMany(options.stream()
                .map(Info::toCommand)
                .toList());
        }

    }

    public record Update(
        @NotBlank
        String name,
        @Min(0)
        Integer price,
        @NotBlank
        String imageUrl,
        @NotNull
        Long categoryId
    ) {

        public ProductCommand.Update toProductCommand() {
            return new ProductCommand.Update(name, price, imageUrl, categoryId);
        }


    }
}
