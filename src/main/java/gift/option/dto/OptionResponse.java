package gift.option.dto;

public class OptionResponse {
    private Long id;
    private String name;
    private Long quantity;
    String productName;

    // constructor
    public OptionResponse(Long id, String name, Long quantity, String productName) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.productName = productName;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
