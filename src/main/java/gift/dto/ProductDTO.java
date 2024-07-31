package gift.dto;

import gift.model.Product;
import gift.model.ProductName;

public class ProductDTO {
    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;
    private Long categoryId;

    public ProductDTO(Long id, String name, int price, String imageUrl, Long categoryId) {
        this.id = id;
        this.setName(name);
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    public void setName(String name) {
        ProductName productName = new ProductName(name);
        this.name = productName.getName();
    }

    public static ProductDTO getProductDTO(Product product){
        return new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getCategory().getId());
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

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getCategoryId() {
        return categoryId;
    }

//    public Long getCategory() {
//        return categoryId;
//    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", categoryId=" + categoryId +
                '}';
    }
}
