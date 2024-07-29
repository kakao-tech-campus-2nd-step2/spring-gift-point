package gift.dto;

public class ProductOptionRequestDto {
    private ProductRequestDto productRequestDto;
    private OptionRequestDto optionRequestDto;

    public ProductOptionRequestDto(ProductRequestDto productRequestDto,
        OptionRequestDto optionRequestDto) {
        this.productRequestDto = productRequestDto;
        this.optionRequestDto = optionRequestDto;
    }

    public ProductRequestDto getProductRequestDto() {
        return productRequestDto;
    }

    public OptionRequestDto getOptionRequestDto() {
        return optionRequestDto;
    }

    public void setProductRequestDto(ProductRequestDto productRequestDto) {
        this.productRequestDto = productRequestDto;
    }

    public void setOptionRequestDto(OptionRequestDto optionRequestDto) {
        this.optionRequestDto = optionRequestDto;
    }
}
