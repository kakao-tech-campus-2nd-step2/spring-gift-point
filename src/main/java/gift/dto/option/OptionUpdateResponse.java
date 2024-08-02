package gift.dto.option;

public class OptionUpdateResponse {
    private Long id;
    private String name;
    private Long quantity;

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Long getQuantity() {
        return quantity;
    }
    public OptionUpdateResponse(Long id, String name, Long quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }
}
