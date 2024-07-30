package gift.entity;

import gift.dto.category.ResponseCategoryDTO;
import gift.dto.category.SaveCategoryDTO;
import gift.exception.exception.BadRequestException;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String name;
    String imageUrl;
    String description;
    String color;

    @OneToMany(mappedBy = "category")
    List<Product> products = new ArrayList<>();

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public Category(SaveCategoryDTO saveCategoryDTO) {
        this.name = saveCategoryDTO.name();
        this.imageUrl = saveCategoryDTO.imageUrl();
        this.color = saveCategoryDTO.color();
        this.description = saveCategoryDTO.description();
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addProduct(Product product) {
        products.add(product);
        product.setCategory(this);
    }

    public int getId() {
        return id;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public void deleteProduct(Product product) {
        this.products.remove(product);
    }

    public ResponseCategoryDTO toResponseDTO() {
        return new ResponseCategoryDTO(this.id, this.name,this.description,this.color,this.imageUrl);
    }

    public void checkEmpty() {
        if (!this.products.isEmpty()) throw new BadRequestException("해당 카테고리에 물품이 존재합니다.");
    }

    public Category updateCategory(SaveCategoryDTO saveCategoryDTO) {
        this.name = saveCategoryDTO.name();
        this.color = saveCategoryDTO.color();
        this.description = saveCategoryDTO.description();
        this.imageUrl = saveCategoryDTO.imageUrl();
        return this;
    }
}
