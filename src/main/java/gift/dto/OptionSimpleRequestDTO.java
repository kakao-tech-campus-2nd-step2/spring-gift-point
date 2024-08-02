package gift.dto;

import gift.model.Option;
import gift.model.Product;

public class OptionSimpleRequestDTO {
    private String name;
    private int quantity;

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public Option toEntity(Product product) {
        return new Option(this.name, this.quantity, product);
    }
}
