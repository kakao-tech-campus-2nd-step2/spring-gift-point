package gift.domain;

public enum Role {
    ADMIN("관리자"), USER("일반");
    private String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
