package gift.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "상품")
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "상품 ID(자동으로 설정)", defaultValue = "1")
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "입력은 공백일 수 없습니다.")
    @Size(max = 15, message = "길이가 15를 넘을 수 없습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ()\\[\\]+&/_ ]*$", message = "( ), [ ], +, -, &, /, _ 외의 특수 문자는 사용이 불가합니다.")
    @Pattern(regexp = "(?!.*카카오).*$",message = "\"카카오\"가 포함된 문구입니다. 담당 MD와 협의 하세요")
    @Schema(description = "상품 이름", defaultValue = "상품 이름")
    private String name;

    @Column(name = "price", nullable = false)
    @Positive(message = "상품의 가격은 0이하일 수 없습니다.")
    @Schema(description = "상품 가격", defaultValue = "1000")
    private int price;

    @Column(name = "imageUrl", nullable = false)
    @Schema(description = "상품 이미지", defaultValue = "상품이미지 url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id",nullable = false)
    @Schema(description = "상품의 카테고리")
    private Category category;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER,cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private List<Option> options;

    protected Product(){

    }

    @ConstructorProperties({"id","name","price","imageUrl","category","options"})
    public Product(Long id, String name, int price, String imageUrl,Category category, List<Option> options) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.options = new ArrayList<>();
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

    public List<Option> getOptions() {
        return options;
    }
}
