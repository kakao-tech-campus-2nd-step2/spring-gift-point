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
    CategoryResponse category;
    List<OptionResponse> options;

    public ProductResponse(){

    }

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.category = new CategoryResponse(product.getCategory());
        this.options = product.getOptions().stream()
                .map(OptionResponse::new)
                .collect(Collectors.toList());
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
  
    public CategoryResponse getCategory(){
        return category;
    }

    public List<OptionResponse> getOptions(){
        return options;
    }
}
