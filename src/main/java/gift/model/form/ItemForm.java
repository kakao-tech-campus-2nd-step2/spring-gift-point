package gift.model.form;

import gift.model.dto.OptionDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "상품 입력 폼")
public class ItemForm {

    private Long id;

    @Schema(description = "상품 이름", example = "스마트폰")
    @NotBlank(message = "이름은 필수 입력입니다.")
    @Size(max = 15, message = "이름은 15자를 넘길 수 없습니다.")
    @Pattern(regexp = "[a-zA-Z0-9가-힣() +\\-&\\[\\]/_]*", message = "(),[],+,-,&,/,_ 를 제외한 특수문자는 사용이 불가합니다.")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "\"카카오\"가 포함된 문구는 담당 MD와 협의한 경우에만 사용 가능합니다.")
    private String name;

    @Schema(description = "상품 가격", example = "500000")
    @NotNull(message = "가격은 필수 입력입니다.")
    @Positive(message = "음수는 입력 불가합니다.")
    private Long price;

    @Schema(description = "상품 이미지 URL", example = "https://example.com/images/smartphone.jpg")
    private String imgUrl;

    @Schema(description = "카테고리 ID", example = "1")
    @NotNull(message = "카테고리는 필수 입력입니다.")
    @Positive(message = "음수는 입력 불가합니다.")
    private Long categoryId;

    @Schema(description = "상품 옵션 목록")
    @Valid
    @NotNull(message = "옵션은 필수 항목입니다.")
    private List<OptionForm> options;

    public ItemForm(String name, Long price, String imgUrl, Long categoryId) {
        this.id = null;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
        this.categoryId = categoryId;
        this.options = new ArrayList<>();
    }

    public ItemForm() {
        this.options = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public ItemForm(Long id, String name, Long price, String imgUrl, Long categoryId,
        List<OptionForm> options) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
        this.categoryId = categoryId;
        this.options = options;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public List<OptionForm> getOptions() {
        return options;
    }

    public List<OptionDTO> getOptionDTOList() {
        return options.stream().map(OptionForm::toDTO).collect(Collectors.toList());
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setOptions(List<OptionForm> options) {
        this.options = options;
    }
}
