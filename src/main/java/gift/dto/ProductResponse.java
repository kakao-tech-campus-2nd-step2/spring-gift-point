package gift.dto;

import gift.entity.Product;
import java.util.List;

public class ProductResponse {

    private Long id;
    private String name;
    private int price;
    private String imgUrl;
    private String categoryName;
    private List<OptionResponse> options;

    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, int price, String imgUrl, String categoryName,
        List<OptionResponse> options) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
        this.categoryName = categoryName;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<OptionResponse> getOptions() {
        return options;
    }

    public static ProductResponse from(Product product) {
        List<OptionResponse> optionResponses = product.getOptions().stream()
            .map(option -> new OptionResponse(option.getId(), option.getName(),
                option.getQuantity()))
            .toList();

        return new ProductResponse(product.getId(), product.getName(), product.getPrice(),
            product.getImgUrl(), product.getCategory().getName(), optionResponses);
    }
}
