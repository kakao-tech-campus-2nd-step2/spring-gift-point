package gift.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Category {

  @OneToMany(mappedBy = "category")
  private final List<Product> products = new ArrayList<>();

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false, length = 7)
  private String color;

  @Column(nullable = false)
  private String imageUrl;

  @Lob
  @Column(nullable = false, columnDefinition = "CLOB")
  private String description;

  protected Category() {
  }

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
