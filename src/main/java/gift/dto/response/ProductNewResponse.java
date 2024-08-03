package gift.dto.response;


import gift.entity.Product;

public class ProductNewResponse {
    private Long productId;
    private String name;
    private int price;
    private String imageUrl;
    private boolean isWish;

    public ProductNewResponse(Long productId, String name, int price, String imageUrl){
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }


    public ProductNewResponse(Product product, boolean isWish) {
        this.productId = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.isWish = isWish;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setWish(boolean wish) {
        isWish = wish;
    }
}