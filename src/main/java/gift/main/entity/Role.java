package gift.main.entity;

public enum Role {
    USER("user"),
    ADMIN("admin");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public static Role toRole(String role) {
        return Role.valueOf(role.toUpperCase());
    }

    public String getRole() {
        return role;
    }
}