package gift.dto.productDTOs;

import gift.model.valueObject.ProductName;

public class UpdateProductDTO {
    private String name;
    private Integer price;
    private String imageUrl;
    private String category;

    public UpdateProductDTO(String name, Integer price, String imageUrl, String category) {
        this.setName(name);
        this.price = price;
        this.imageUrl = imageUrl;
        this.setCategory(category);
    }

    public void setName(String name) {
        ProductName productName = new ProductName(name);
        this.name = productName.getName();
    }

    public void setCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            this.category = "NONE";
        }
        else {
            this.category = category;
        }
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    @Override
    public String toString() {
        return "SaveProductDTO{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

}
