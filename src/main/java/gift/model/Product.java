package gift.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank(message = "상품 이름을 입력해주세요.")
        @Size(max = 255, message = "상품 이름은 255자 이내여야 합니다.")
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9\\s\\(\\)\\[\\]+&\\-/_]*$", message = "상품 이름에 유효하지 않은 문자가 포함되어 있습니다.")
        @Column(nullable = false)
        private String name;

        @Min(value = 0, message = "상품 가격은 0원 이상이어야 합니다.")
        @Column(nullable = false)
        private int price;

        @Column(nullable = false, name = "image_url")
        private String imageUrl;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "category_id", nullable = false)
        private Category category;

        @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
        private Set<Option> options = new HashSet<>();

        protected Product() {}

        public Product(Long id, String name, int price, String imageUrl, Category category) {
                this.id = id;
                this.name = name;
                this.price = price;
                this.imageUrl = imageUrl;
                this.category = category;
        }

        public Long getId() {
                return id;
        }

        public String getName() {
                return name;
        }

        public int getPrice() {
                return price;
        }

        public String getImageUrl() {
                return imageUrl;
        }

        public Category getCategory() {
                return category;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public void setName(String name) {
                this.name = name;
        }

        public void setPrice(int price) {
                this.price = price;
        }

        public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
        }

        public void setCategory(Category category) {
                this.category = category;
        }

        public Set<Option> getOptions() {
                return options;
        }

        public void setOptions(Set<Option> options) {
                this.options = options;
        }
}