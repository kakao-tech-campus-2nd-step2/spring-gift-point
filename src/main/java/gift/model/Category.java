package gift.model;

import jakarta.persistence.*;

@Entity
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false, length = 255)
  private String name;

  @Column(name = "color", nullable = false, length = 7)
  private String color;

  @Column(name = "image_url", length = 255)
  private String imageUrl;

  @Column(name = "description", length = 255)
  private String description;

  protected Category() {
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
