package gift.entity;

import gift.exception.BadRequestExceptions.BadRequestException;
import gift.exception.BadRequestExceptions.BlankContentException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;
import java.util.regex.Pattern;

@Entity
public class Category {

    private static final Pattern COLOR_PATTERN = Pattern.compile("^#[0-9a-fA-F]{6}$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false, unique = true) //Candidate Key
    private String name;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String description;

    protected Category() { }

    public Category(String name, String color, String imageUrl, String description) {
        validateName(name);
        validateColor(color);
        validateImageUrl(imageUrl);
        validateDescription(description);

        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getColor() { return color; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }

    public void changeCategory(String name, String color, String imageUrl, String description) {
        validateName(name);
        validateColor(color);
        validateImageUrl(imageUrl);
        validateDescription(description);

        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }


    private void validateName(String name){
        if(name.isBlank())
            throw new BlankContentException("이름을 입력해주세요");
    }

    private void validateColor(String color){
        if(color.isBlank())
            throw new BlankContentException("색상을 입력해주세요");

        if(!COLOR_PATTERN.matcher(color).matches())
            throw new BadRequestException("색상 코드가 아닙니다.");
    }

    private void validateImageUrl(String imageUrl){
        if(imageUrl.isBlank())
            throw new BlankContentException("이미지 url을 입력해주세요.");
    }

    private void validateDescription(String description){
        if(description == null)
            throw new BadRequestException("잘못된 description 입니다.");
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Category category = (Category) object;
        return Objects.equals(getId(), category.getId()) && Objects.equals(
                getColor(), category.getColor()) && Objects.equals(getImageUrl(),
                category.getImageUrl()) && Objects.equals(getDescription(),
                category.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getColor(), getImageUrl(), getDescription());
    }
}
