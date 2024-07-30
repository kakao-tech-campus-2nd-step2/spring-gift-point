package gift.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProductDto {

    private long id;
    @Size(min = 1, max = 15, message = "글자 수는 1자 이상, 15자 이하여야 합니다.")
    @Pattern(regexp = "^[\\w\\s()+\\-/&_\\[\\]가-힣]*$", message = "( ), [ ], +, -, &, /, _" +
            "외의 다른 특수 문자 사용 불가")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "\"카카오\"가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.")
    private String name;
    private long categoryId;
    private int price;
    private String imageUrl;
    private boolean isDeleted;

    public ProductDto() {

    }

    public ProductDto(long id, String name, long categoryId, int price, String imageUrl, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isDeleted = isDeleted;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
}
