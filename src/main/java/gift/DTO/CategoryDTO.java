package gift.DTO;

import java.util.ArrayList;
import java.util.List;

public class CategoryDTO {

    private Long id;
    private String name;
    private Long parentId;
    private List<CategoryDTO> children = new ArrayList<>();
    private List<ProductDTO> products = new ArrayList<>();

    public CategoryDTO(Long id, String name) {}



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

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public void setChildren(List<CategoryDTO> children) {
        this.children = children;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

}
