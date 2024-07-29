package gift.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Category {

    private Long id;

    @NotBlank(message = "카테고리 이름을 반드시 입력해 주세요")
    private String name;

    @NotBlank(message = "색깔의 HEX CODE를 반드시 입력해 주세요")
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "HEX CODE 형식에 맞추어 작성해 주세요")
    private String color;

    @NotBlank(message = "이미지URL을 입력해 주세요.")
    @Pattern(regexp = "^(http(s?):)([/|.\\w|\\s|-])*\\.(?:jpg|gif|png)$", message = "URL 형식에 맞추어 작성해주세요")
    private String imageUrl;

    @NotBlank(message = "상세 설명을 적어 주세요." )
    @Size(min = 1, max = 80, message = "1~80자 사이로 적어 주세요")
    private String description;

    public Category() {

    }

    public Category(String name, String color, String imageUrl, String description) {
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Category(Long id, String name, String color, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
