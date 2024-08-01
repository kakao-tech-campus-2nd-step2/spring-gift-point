package gift.DTO.Product;

import gift.DTO.Category.CategoryResponse;
import gift.DTO.Option.OptionResponse;
import gift.domain.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductResponse {
    Long id;
    String name;
    int price;
    String imageUrl;

    public ProductResponse(){

    }

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
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
}
