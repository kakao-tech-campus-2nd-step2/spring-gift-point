package gift.dto.response;

import gift.domain.Option;

public record ProductOptionResponse(Long id, String name, Integer quantity, Long productId) {
    public ProductOptionResponse(Option option) {
        this(option.getId(), option.getName(), option.getQuantity(), option.getProduct().getId());
    }
}
