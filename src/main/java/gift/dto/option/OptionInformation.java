package gift.dto.option;

public record OptionInformation(Long id, String productName, Integer price, String name) {
    public static OptionInformation of(Long id, String productName, Integer price, String name) {
        return new OptionInformation(id, productName, price, name);
    }
}
