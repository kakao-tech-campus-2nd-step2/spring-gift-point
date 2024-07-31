package gift.DTO;

import gift.Model.Entity.Option;
import gift.Model.Entity.Product;

import java.util.List;

public class ResponseProductDTO {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private List<ResponseOptionDTO> options;

    public ResponseProductDTO(Long id, String name, int price, String imageUrl, List<ResponseOptionDTO> options) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.options = options;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<ResponseOptionDTO> getOptions() {
        return options;
    }

    public static ResponseProductDTO of(Product product, List<ResponseOptionDTO> options) {
        return new ResponseProductDTO(product.getId(), product.getName().getValue(), product.getPrice().getValue(), product.getImageUrl().getValue(), options);
    }
}
