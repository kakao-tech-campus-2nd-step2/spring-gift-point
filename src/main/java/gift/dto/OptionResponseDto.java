package gift.dto;

public class OptionResponseDto {
    private Long id;
    private String name;

    public OptionResponseDto() {
    }

    public OptionResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}