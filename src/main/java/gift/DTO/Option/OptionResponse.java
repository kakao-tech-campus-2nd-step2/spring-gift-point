package gift.DTO.Option;

import gift.domain.Option;

public class OptionResponse {
    Long id;
    String name;
    int quantity;
    Long productId;

    public OptionResponse(){

    }

    public OptionResponse(Option option, Long productId) {
        this.id = option.getId();
        this.name = option.getName();
        this.quantity = option.getQuantity();
        this.productId = productId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
    public Long getProductId(){
        return productId;
    }
}
