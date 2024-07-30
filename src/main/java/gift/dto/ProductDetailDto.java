package gift.dto;

import gift.model.Product;

public class ProductDetailDto {
    private String name;
    private int price;
    private String imageUrl;

    public ProductDetailDto(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public ProductDetailDto(Product product) {
        this(product.getName(), product.getPrice(), product.getImageUrl());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
