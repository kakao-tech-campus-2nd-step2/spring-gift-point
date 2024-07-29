package gift.product.dto;

import static gift.product.exception.GlobalExceptionHandler.NOT_EXIST_ID;

import gift.product.exception.InvalidIdException;
import gift.product.model.Option;
import gift.product.repository.ProductRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class OptionDTO {

    @NotBlank
    private String name;
    @PositiveOrZero
    private int quantity;

    public OptionDTO() {

    }

    public OptionDTO(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Option convertToDomain(Long productId, ProductRepository productRepository) {
        return new Option(
            name,
            quantity,
            productRepository.findById(productId)
                .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID))
        );
    }

    public Option convertToDomain(Long id, Long productId, ProductRepository productRepository) {
        return new Option(
            id,
            name,
            quantity,
            productRepository.findById(productId)
                .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID))
        );
    }
}
