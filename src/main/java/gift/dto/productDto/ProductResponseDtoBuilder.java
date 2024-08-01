package gift.dto.productDto;

public class ProductResponseDtoBuilder {
    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;
    private Long categoryId;

    public ProductResponseDtoBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ProductResponseDtoBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ProductResponseDtoBuilder price(Integer price) {
        this.price = price;
        return this;
    }

    public ProductResponseDtoBuilder imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public ProductResponseDtoBuilder categoryId(Long categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public ProductResponseDto build() {
        return new ProductResponseDto(id,name,price,imageUrl,categoryId);
    }
}