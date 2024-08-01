package gift.dto.Response;

import java.util.List;

public class WishResponseDto {
    private Long id;
    private ProductResponseDto productResponseDto;
    private int count;
    private List<OptionResponseDto> options; // 추가된 필드

    public WishResponseDto(Long id, ProductResponseDto productResponseDto, int count, List<OptionResponseDto> options) {
        this.id = id;
        this.productResponseDto = productResponseDto;
        this.count = count;
        this.options = options; // 필드 초기화
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductResponseDto getProductResponseDto() {
        return productResponseDto;
    }

    public void setProductResponseDto(ProductResponseDto productResponseDto) {
        this.productResponseDto = productResponseDto;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<OptionResponseDto> getOptions() {
        return options;
    }

    public void setOptions(List<OptionResponseDto> options) {
        this.options = options;
    }
}
