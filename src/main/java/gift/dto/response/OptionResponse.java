package gift.dto.response;

public class OptionResponse {
    private Long id;
    private String name;
    private Integer quantity;

    public OptionResponse(Long id, String name, Integer quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }
}