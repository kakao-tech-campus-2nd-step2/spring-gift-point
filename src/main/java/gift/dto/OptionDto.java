package gift.dto;

public class OptionDto {

    private final Long id;
    private final String name;
    private final int amount;
    private final ProductDto ProductDto;

    public OptionDto(Long id, String name, int amount, gift.dto.ProductDto productDto) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        ProductDto = productDto;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }
}
