package gift.DTO;

import jakarta.validation.constraints.Size;

public class CategoryDto {

  private Long id;
  private String name;
  @Size(min = 7, max = 7)
  private String color;
  private String imageUrl;
  private String description;

  public CategoryDto() {
  }

  public CategoryDto(Long id, String name, String color, String imageUrl, String description) {
    this.id = id;
    this.name = name;
    this.color = color;
    this.imageUrl = imageUrl;
    this.description = description;
  }

  public CategoryDto(String name, String color, String imageUrl, String description) {
    this.name = name;
    this.color = color;
    this.imageUrl = imageUrl;
    this.description = description;
  }

  public Long getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getColor() {
    return this.color;
  }

  public String getImageUrl() {
    return this.imageUrl;
  }

  public String getDescription() {
    return this.description;
  }
}
