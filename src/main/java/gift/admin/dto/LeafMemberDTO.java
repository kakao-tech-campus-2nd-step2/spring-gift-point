package gift.admin.dto;

public class LeafMemberDTO {

    private Long id;
    private String email;
    private Integer point;

    public LeafMemberDTO() {
    }

    public LeafMemberDTO(Long id, String email, Integer point) {
        this.id = id;
        this.email = email;
        this.point = point;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
}
