package gift.converter;

import gift.dto.OptionDTO;
import gift.dto.ProductDTO;
import gift.model.Option;
import gift.model.Product;
import java.util.List;
import java.util.stream.Collectors;

public class ProductConverter {

    public static ProductDTO convertToDTO(Product product) {
        List<OptionDTO> optionDTOs = product.getOptions().stream()
            .map(OptionConverter::convertToDTO)
            .collect(Collectors.toList());
        return new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getCategoryId(), optionDTOs);
    }

    public static Product convertToEntity(ProductDTO productDTO) {
        List<Option> options = productDTO.getOptions().stream()
            .map(optionDTO -> {
                Option option = OptionConverter.convertToEntity(optionDTO);
                option.assignProduct(null);
                return option;
            }).collect(Collectors.toList());

        Product product = new Product(
            productDTO.getId(),
            productDTO.getName(),
            productDTO.getPrice(),
            productDTO.getImageUrl(),
            productDTO.getCategoryId(),
            options
        );

        options.forEach(option -> option.assignProduct(product));

        return product;
    }
}