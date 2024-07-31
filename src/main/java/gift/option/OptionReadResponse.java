package gift.option;

import gift.product.ProductReadResponse;

public record OptionReadResponse(Long id, String name, Long quantity, ProductReadResponse product) {

    public OptionReadResponse(Option option) {
        this(option.getId(), option.getName(), option.getQuantity(), new ProductReadResponse(option.getProduct()));
    }

}
