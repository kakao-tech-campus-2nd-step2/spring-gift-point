package gift.entity;

import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
public class Product {
   @Positive(message = "price must be positive")
   @Column(nullable = false)
   private int price;

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @NotBlank(message = "name must not be blank")
   @Size(max = 15, message = "name must be less than 15 characters")
   @Column(nullable = false, length = 15)
   private String name;

   @NotBlank(message = "image url must not be blank")
   @Column(nullable = false, length = 255)
   private String imageUrl;

    public Product(Long id, String name, int price, String imageUrl) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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


}
