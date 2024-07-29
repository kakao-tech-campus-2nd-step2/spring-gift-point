package gift.dto;

public class UserResponseDto {
    private final Long id;
    private final String email;

    public UserResponseDto(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}