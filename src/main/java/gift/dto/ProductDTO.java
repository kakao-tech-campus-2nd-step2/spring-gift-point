package gift.dto;

import gift.model.Product;
import gift.model.ProductName;

public class ProductDTO {
    private long id;
    private String name;
    private Integer price;
    private String imageUrl;
    private String category;

    public ProductDTO(long id, String name, int price, String imageUrl, String category) {
        this.id = id;
        this.setName(name);
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public void setName(String name) {
        ProductName productName = new ProductName(name);
        this.name = productName.getName();
    }

    public static ProductDTO getProductDTO(Product product){
        return new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getCategory().getName());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCategory() {
        return category;
    }
}
