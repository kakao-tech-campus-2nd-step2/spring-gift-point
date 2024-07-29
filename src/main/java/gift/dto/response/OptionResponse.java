package gift.dto.response;

public class OptionResponse {

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

    public OptionResponse(Long id, String name, Long quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

}
