package gift.dto.option;

public record OptionResponse(Long id, String name, Integer quantity) {
    public static OptionResponse of(Long id, String name, Integer quantity) {
        return new OptionResponse(id, name, quantity);
    }
}
