package gift.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import gift.dto.request.OptionRequestDTO;
import gift.dto.request.ProductRequestDTO;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "price")
    private Integer price;

    @Column(nullable = false, name = "image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "product", orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "product", orphanRemoval = true)
    private List<Wish> wishes = new ArrayList<>();

    public Product() {}

    public Product(String name, Integer price, String imageUrl, Category category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
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

    public Category getCategory() {
        return category;
    }

    public List<Option> getOptions() {
        return options;
    }

    public List<Wish> getWishes() {
        return wishes;
    }

    public void updateProduct(ProductRequestDTO productRequestDTO){
        this.name=productRequestDTO.name();
        this.price=productRequestDTO.price();
        this.imageUrl=productRequestDTO.imageUrl();
    }




    public void addOption(OptionRequestDTO optionRequestDTO){
        Option option = new Option(
                optionRequestDTO.name(),
                optionRequestDTO.quantity(),
                this
        );
        this.options.add(option);
    }

    public boolean existsOptionName(String optionName){
        for(Option option : options){
            if(option.getName()
                    .equals(optionName)){
                return true;
            }
        }
        return false;
    }
}