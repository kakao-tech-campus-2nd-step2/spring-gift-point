package gift.dto;

public class ProductResponseDto {

    private final String name;
    private final String url;
    private final Integer price;
    private final Long id;

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
