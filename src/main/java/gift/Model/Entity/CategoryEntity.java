package gift.Model.Entity;

import gift.Model.DTO.CategoryDTO;
import jakarta.persistence.*;

@Entity
@Table(name="category")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="optionName")
    private String name;

    @Column(name="image_url")
    private String imageUrl;

    @Column(name="description")
    private String description;

    public CategoryEntity(){}

    public CategoryEntity(String name, String imageUrl, String description){
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public CategoryDTO mapToDTO(){
        return new CategoryDTO(this.id, this.name, this.imageUrl,this.description);
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

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
