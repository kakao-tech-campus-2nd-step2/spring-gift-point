package gift.dto;

public class OptionRequest {


    private Long id;
    private String name;
    private int quantity;
    private String productName;

    public OptionRequest() {
    }

    public OptionRequest(String name, int quantity, String productName) {
        this(null, name, quantity, productName);
    }

    public OptionRequest(Long id, String name, int quantity, String productName) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.productName = productName;
    }

    public String getName() {
        return name;
    }

    public OptionRequest setName(String name) {
        this.name = name;
        return this;
    }

    public Long getId() {
        return id;
    }
    public int getQuantity() {
        return quantity;
    }

    public OptionRequest setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public OptionRequest setProductName(String productName) {
        this.productName = productName;
        return this;
    }
}
