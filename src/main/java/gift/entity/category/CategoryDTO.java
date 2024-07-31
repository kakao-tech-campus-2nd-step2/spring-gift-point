package gift.entity;


public class CategoryDTO {
    Long category_id;
    private String name;

    public CategoryDTO(Category category) {
        this.category_id = category.getId();
        this.name = category.getName();
    }

    public CategoryDTO() {
    }

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
