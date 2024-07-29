package gift.dto;

public class MemberRequestDto {

    private final Long id;
    private final String email;
    private final String password;

    public MemberRequestDto(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public MemberRequestDto(String email, String password) {
        this(null, email, password);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
