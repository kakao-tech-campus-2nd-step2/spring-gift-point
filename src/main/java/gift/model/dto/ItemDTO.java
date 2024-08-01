package gift.model.dto;

import gift.model.entity.Item;

public class ItemDTO {

    private final Long id;
    private final String name;
    private final Long price;
    private final String imageUrl;
    private final Long categoryId;

    public ItemDTO(Long id, String name, Long price, String imageUrl, Long categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    public ItemDTO(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.imageUrl = item.getImgUrl();
        this.categoryId = item.getCategory().getId();
    }

    public ItemDTO(String name, Long price, String imageUrl, Long categoryId) {
        this(null, name, price, imageUrl, categoryId);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getCategoryId() {
        return categoryId;
    }
}