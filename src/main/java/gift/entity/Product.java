package gift.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import gift.dto.product.SaveProductDTO;
import gift.dto.product.ResponseProductDTO;
import gift.exception.exception.BadRequestException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotBlank(message = "이름 공백 안됨")
    @Size(max = 50, message = "50글자까지만 가능")
    @Pattern(regexp = "^[a-zA-Z0-9()\\[\\]+\\-&/_]+$", message = "특수기호 안됨")
    @Pattern(regexp = "^(?!.*카카오).*", message = "카카오는 md와 상담")
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    int price;
    @Column(nullable = false)
    String imageUrl;

    @OneToMany(mappedBy = "product")
    @Size(max = 100_000_000)//1~1억
    List<Option> options = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    List<WishList> wishlists = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    Category category;

    public Product() {
    }

    public Product(String name, int price, String imageUrl, Category category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Product(SaveProductDTO saveProductDTO, Category category) {
        this.name = saveProductDTO.name();
        this.price = saveProductDTO.price();
        this.imageUrl = saveProductDTO.imageUrl();
        this.category = category;
    }

    public List<Option> getOptions() {
        return this.options;
    }

    public String getCategoryName() {
        return category.getName();
    }

    public int getId() {
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

    public void addWishlist(WishList wishlist) {
        wishlists.add(wishlist);
    }

    public void deleteWishlist(WishList wishlist) {
        this.wishlists.remove(wishlist);
    }

    public void addOptions(Option option) {
        options.add(option);
        option.setProduct(this);
    }

    public void deleteOption(Option option) {
        if (this.options.size() == 1) throw new BadRequestException("마지막 옵션 삭제 불가");
        options.remove(option);
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Product modifyProduct(SaveProductDTO modifyProductDTO) {
        this.name = modifyProductDTO.name();
        this.price = modifyProductDTO.price();
        this.imageUrl = modifyProductDTO.imageUrl();
        return this;
    }

    public ResponseProductDTO toResponseDTO() {
        return new ResponseProductDTO(id, name, price, imageUrl);
    }
}
