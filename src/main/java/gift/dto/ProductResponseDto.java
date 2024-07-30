package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 응답 DTO")
public class ProductResponseDto {
    @Schema(description = "상품 고유 id")
    private Long id;
    @Schema(description = "상품 이름")
    private String name;
    @Schema(description = "상품 가격")
    private Integer price;
    @Schema(description = "상품 url")
    private String url;

    public ProductResponseDto(Long id, String name, Integer price, String url) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.url = url;
    }

    public ProductResponseDto(String name, Integer price, String url) {
        this(null, name, price, url);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getUrl() {
        return url;
    }

}
