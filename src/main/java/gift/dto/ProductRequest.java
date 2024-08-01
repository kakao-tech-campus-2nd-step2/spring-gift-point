package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.model.Option;
import gift.model.Product;
import gift.repository.CategoryRepository;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.ArrayList;

public class ProductRequest {

//    @NotNull
//    private Long id;

//    @NotNull
    @Size(min = 1, max = 15)
    private String name;

//    @NotNull
    private int price;

//    @NotNull
    @JsonProperty("image_url")
    private String imageUrl;

//    @NotNull
    @JsonProperty("category_id")
    private Long categoryId;

//    @NotNull
    private List<OptionRequest> options;

    public ProductRequest() {
    }

//    public Product DtoToProductEntity(){
//        Product product = new Product();
//        product.setName(this.name);
//        product.setPrice(this.price);
//        product.setImageUrl(this.imageUrl);
//        product.setCategory(categoryRepository.findById(this.categoryId).get());
//
//        return product;
//    }

    public List<Option> dtoToOptionEntity(Product product){
        List<Option> options = new ArrayList<>();

        for (int i = 0; i < this.options.size(); i++) {
            String optionName = this.options.get(i).getName();
            Long optionQuantity = this.options.get(i).getQuantity();
            OptionRequest optionRequest = new OptionRequest();
            optionRequest.setName(optionName);
            optionRequest.setQuantity(optionQuantity);
            Option option = new Option(optionRequest, product);
            options.add(option);
        }

        return options;
    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }


    public @Size(min = 1, max = 15) String getName() {
        return name;
    }

    public void setName(@Size(min = 1, max = 15) String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }


    public String getImageUrl() {
        return imageUrl;
    }


    public Long getCategoryId() {
        return categoryId;
    }


    public List<OptionRequest> getOptions() {
        return options;
    }

    public void setOptions(List<OptionRequest> options) {
        this.options = options;
    }
}
