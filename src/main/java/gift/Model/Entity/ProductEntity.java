package gift.Model.Entity;

import jakarta.persistence.*;

@Entity
@Table(name="product")
public class ProductEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name="category_id")
        private CategoryEntity category;

        @Column(name="name")
        private String name;

        @Column(name="price")
        private int price;

        @Column(name = "image_url")
        private String imageUrl;

        public ProductEntity(){}

        public ProductEntity(CategoryEntity category, String name, int price, String imageUrl){
                this.category = category;
                this.name = name;
                this.price = price;
                this.imageUrl = imageUrl;
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


