package gift.dto.option;

public record OptionResponse(Long id, String name, Long quantity) {
    public OptionResponse(Long id, String name, Long quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        Object product = null;
    }
}
