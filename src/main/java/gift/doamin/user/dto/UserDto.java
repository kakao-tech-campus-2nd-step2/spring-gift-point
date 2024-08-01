package gift.doamin.user.dto;

import gift.doamin.user.entity.User;
import gift.doamin.user.entity.UserRole;

public class UserDto {

    Long id;
    String email;
    String name;
    UserRole role;

    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.role = user.getRole();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public UserRole getRole() {
        return role;
    }
}
