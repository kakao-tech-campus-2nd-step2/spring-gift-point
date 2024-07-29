package gift.DTO.Category;

public class CategoryRequest {
    String name;

    public CategoryRequest(){

    }

    public CategoryRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
