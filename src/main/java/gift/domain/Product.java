package gift.domain;

import gift.constants.Messages;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name="price",nullable = false)
    private int price;

    @Column(name = "imageUrl",nullable = false)
    private String imageUrl;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Option> options = new ArrayList<>();
    public Product() {}

    public Product(String name, int price, String imageUrl, Category category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }
    // getter
    public long getId() {
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

    public Category getCategory() {
        return category;
    }

    public List<Option> getOptions(){ return  options;}

    // setter
    public void setId(long id) {
        this.id = id;
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

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setOption(Option option){
        validateDuplicateOptionName(options, option.getName());
        this.options.add(option);
    }

    public void deleteOption(Long optionId){
        Option option = options.stream()
            .filter(o -> o.getId().equals(optionId))
            .findAny()
            .get();
        option.removeProduct();
        options.remove(option);
    }

    // 각 상품이 가진 옵션 이름 중복 검사
    private void validateDuplicateOptionName(List<Option> options,String name){
        for(Option option:options){
            if(option.getName().equals(name)){
                throw new IllegalArgumentException(Messages.DUPLICATE_OPTION_NAME_MESSAGE);
            }
        }
    }
}
