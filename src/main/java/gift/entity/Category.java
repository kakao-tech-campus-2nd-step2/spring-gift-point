package gift.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

import gift.dto.CategoryDto;
import gift.dto.request.CategoryRequest;

@Entity
@Table(name = "category")
public class Category {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String color;
    private String imageUrl;
    private String description;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Product> products;

    public Category(){

    }

    public Category(String name, String color, String imageUrl, String description) {
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor(){
        return color;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public String getDescription(){
        return description;
    }
    
    public List<Product> getProducts(){
        return products;
    }

    public CategoryDto update(CategoryRequest categoryRequest){
        this.name = categoryRequest.getName();
        this.color = categoryRequest.getColor();
        this.imageUrl = categoryRequest.getImageUrl();
        this.description = categoryRequest.getDescription();
        return new CategoryDto(this.id, this.name, this.color, this.imageUrl, this.description);
    }
    
}
