package gift.category.entity;

import gift.category.dto.CategoryRequestDto;
import gift.product.entity.Product;
import gift.util.BaseEntity;
import jakarta.persistence.*;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private String color;

  @Column(nullable = false)
  String imageUrl;

  private String description;

  @OneToMany(mappedBy = "category")
  private List<Product> products;

  public Category(String name, String color, String imageUrl, String description) {
    this.name = name;
    this.color = color;
    this.imageUrl = imageUrl;
    this.description = description;
  }

  public void update(CategoryRequestDto categoryRequestDto) {
    this.name = categoryRequestDto.name();
    this.color = categoryRequestDto.color();
    this.imageUrl = categoryRequestDto.imageUrl();
    this.description = categoryRequestDto.description();
  }
}
