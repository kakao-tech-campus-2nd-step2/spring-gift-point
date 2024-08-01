package gift.category.dto;

// 입력을 카테고리 관련 정보로 포장하는 dto
public record CategoryRequestDto(
    String name,
    String imageUrl
) {

}
