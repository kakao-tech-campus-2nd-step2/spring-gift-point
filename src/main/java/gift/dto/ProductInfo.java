package gift.dto;

import gift.entity.Product;

public class ProductInfo {
    
    private Long id;
    private String name;
    private int price;
    private String imageUrl;

    public ProductInfo(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
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

    public static ProductInfo fromEntity(Product product){
        return new ProductInfo(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }
}
