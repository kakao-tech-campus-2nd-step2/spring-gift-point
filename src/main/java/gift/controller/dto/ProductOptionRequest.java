package gift.controller.dto;

import jakarta.validation.constraints.NotEmpty;

public class ProductOptionRequest {

    @NotEmpty(message = "Product Name can not be empty")
    private String name;
    @NotEmpty(message = "Product Quantity can not be empty")
    private int quantity;

    public ProductOptionRequest(String name, int quantity) {
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
}
