package gift.domain;

import gift.dto.CategoryDto;
import gift.dto.OptionDto;
import gift.dto.ProductDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    protected Product() {
    }

    public Product(String name, double price, String imageUrl, Category category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Product(Long id, String name, double price, String imageUrl, Category category,
        List<Option> options) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.options = options;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public CategoryDto getCategoryDto() {
        CategoryDto categoryDto = new CategoryDto(category.getId(), category.getName(),
            category.getColor(), category.getImageUrl(), category.getDescription());
        return categoryDto;
    }

    public List<OptionDto> getOptionDtos() {
        List<OptionDto> optionDtos = new ArrayList<>();
        for (Option option : options) {
            OptionDto optionDto = new OptionDto(option.getId(), option.getName(),
                option.getAmount(), option.getProductDto());
            optionDtos.add(optionDto);
        }
        return optionDtos;
    }

    public void update(String name, double price, String imageUrl, Category category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public ProductDto toProductDto() {
        return new ProductDto(this.getId(), this.getName(), this.getPrice(), this.getImageUrl(),
            this.getCategoryDto(), this.getOptionDtos());
    }
}