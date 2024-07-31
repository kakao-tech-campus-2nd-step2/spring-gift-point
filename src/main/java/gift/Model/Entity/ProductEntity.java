package gift.Model.Entity;

import gift.Model.DTO.ProductDTO;
import jakarta.persistence.*;

@Entity
@Table(name="product")
public class ProductEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name="optionName")
        private String name;

        @Column(name="price")
        private int price;

        @Column(name = "image_url")
        private String imageUrl;

        @ManyToOne
        @JoinColumn(name="category_id")
        private CategoryEntity category;

        public ProductEntity(){}

        public ProductEntity(String name, int price, String imageUrl, CategoryEntity category){
                this.category = category;
                this.name = name;
                this.price = price;
                this.imageUrl = imageUrl;
        }

        public ProductDTO mapToDTO(boolean state){
                return new ProductDTO(this.id, this.name, this.price, this.imageUrl, this.category.getId(), state);
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

        public int getPrice() {
                return price;
        }

        public void setPrice(int price) {
                this.price = price;
        }

        public String getImageUrl() {
                return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
        }
        public CategoryEntity getCategory() {
                return category;
        }

        public void setCategory(CategoryEntity category) {
                this.category = category;
        }
}


