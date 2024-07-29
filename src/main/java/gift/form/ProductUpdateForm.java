package gift.form;

import gift.validation.NoKakao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

public class ProductUpdateForm {

    @NotNull
    private Long id;

    @NotBlank(message = "상품명을 입력해주세요.")
    @Length(min=1, max=15, message = "1~15자 사이로 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣0-9()+&/_ \\[\\]-]*$", message = "상품명애 특수 문자는 '(, ), [, ], +, -, &, /, - 만 입력 가능합니다.'")
    @NoKakao
    private String name;

    @NotNull(message = "가격을 입력해주세요.")
    @Range(min = 100, max = 1000000, message = "상품 가격은 100원~1,000,000원 사이여야 합니다.")
    private Integer price;

    @NotBlank(message = "이미지 주소를 입력해주세요.")
    @Pattern(regexp = "^(https?)://[^ /$.?#].[^ ]*$", message = "올바른 url이 아닙니다.")
    private String imageUrl;


    public ProductUpdateForm() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
