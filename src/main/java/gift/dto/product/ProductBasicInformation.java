package gift.dto.product;

public record ProductBasicInformation(Long id, String name, Integer price) {
    public static ProductBasicInformation of(Long id, String name, Integer price) {
        return new ProductBasicInformation(id, name, price);
    }
}
