package gift.dto.productDTOs;

import gift.model.valueObject.ProductName;

public class InputProductDTO {
    private String name;
    private Integer price;
    private String imageUrl;
    private String category;
    private String option;
    private String quantity;

    public InputProductDTO(String name, int price, String imageUrl, String category, String option, String quantity) {
        this.setName(name);
        this.price = price;
        this.imageUrl = imageUrl;
        this.setCategory(category);
        this.setOption(option);
        this.quantity = quantity;
    }

    public void setName(String name) {
        ProductName productName = new ProductName(name);
        this.name = productName.getName();
    }

    public void setCategory(String category) {
        if (category == null) {
            throw new IllegalArgumentException("카테고리 등록은 필수입니다.");
        }
        this.category = category;
    }

    public void setOption(String option) {
        if (option == null) {
            throw new IllegalArgumentException("옵션은 한 개 이상 등록해야 합니다.");
        }
        this.option = option;
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

    public String getOption() {
        return option;
    }

    public String getQuantity() {
        return quantity;
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
