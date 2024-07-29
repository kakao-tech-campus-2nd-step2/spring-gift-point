package gift.model;

import gift.dto.ProductDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length = 20)
    @NotNull(message = "이름에 NULL 불가능")
    @Size(max = 20, message = "20자 이상 불가능")
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(name = "image_url", nullable = false)
    @NotNull(message = "URL에 NULL 불가능")
    private String imageUrl;

    @Column(name = "category", nullable = false)
    private int category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options;

    // 생성자
    public Product() {
    }

    public Product(Long id, String name, int price, String imageUrl, int category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    //getter
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
    public int getCategory() {
        return category;
    }
    public List<Option> getOptions() {
        return options;
    }

    // setter
    public void setId(Long id) {
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
    public void setCategory(int category) {
        this.category = category;
    }
    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public Product(ProductDTO productDTO) {
        this.id = productDTO.getId();
        this.name = productDTO.getName();
        this.price = productDTO.getPrice();
        this.imageUrl = productDTO.getImageUrl();
        this.category = productDTO.getCategory();
    }

    public void updateFromDTO(ProductDTO productDTO) {
        this.name = productDTO.getName();
        this.price = productDTO.getPrice();
        this.imageUrl = productDTO.getImageUrl();
        this.category = productDTO.getCategory();
    }
}
