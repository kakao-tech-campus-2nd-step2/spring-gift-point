package gift.dto.betweenClient.product;

import gift.dto.betweenClient.option.OptionResponseDTO;
import gift.entity.Product;
import java.util.List;

public record OneProductResponseDTO(
        Long id, String name, Integer price, String imageUrl, List<OptionResponseDTO> options
) {
    public static OneProductResponseDTO convertToDTO(Product product, List<OptionResponseDTO> options) {
        return new OneProductResponseDTO(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), options);
    }
}
