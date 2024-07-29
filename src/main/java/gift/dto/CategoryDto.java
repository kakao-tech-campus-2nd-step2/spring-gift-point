package gift.dto;

import gift.dto.response.CategoryResponse;

public class CategoryDto {

    private Long id;
    private String name;

    public CategoryDto(String name) {
        this.name = name;
    }

    public CategoryDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public CategoryResponse toResponseDto() {
        return new CategoryResponse(this.id, this.name);
    }

}
