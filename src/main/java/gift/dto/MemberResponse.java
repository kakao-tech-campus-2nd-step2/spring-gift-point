package gift.dto;

public class MemberResponse {

    private Long id;
    private String email;

    public MemberResponse(Long id, String email) {
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
