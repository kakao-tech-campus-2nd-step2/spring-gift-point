package gift.web.dto.form;

import gift.domain.Product;
import java.net.URL;

public class UpdateProductForm {

    private final Long id;
    private final String name;
    private final Integer price;
    private final URL imageUrl;

    private UpdateProductForm(Long id, String name, Integer price, URL imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static UpdateProductForm fromEntity(Product product) {
        return new UpdateProductForm(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
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

    public URL getImageUrl() {
        return imageUrl;
    }

}
