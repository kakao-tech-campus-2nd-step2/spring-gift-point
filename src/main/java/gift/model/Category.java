package gift.model;

import jakarta.persistence.*;

@Entity
@Table(name = "category")
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private String color;
  private String imageUrl;
  private String description;

  public Category(Long id, String name, String color, String imageUrl, String description) {
    this.id = id;
    this.name = name;
    this.color = color;
    this.imageUrl = imageUrl;
    this.description = description;
  }

  public Category(String name, String color, String imageUrl, String description) {
    this.name = name;
    this.color = color;
    this.imageUrl = imageUrl;
    this.description = description;
  }

  public Category() {
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getColor() {
    return color;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getDescription() {
    return description;
  }
}