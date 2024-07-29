package gift.user.infrastructure.persistence.entity;

import gift.core.BaseEntity;
import gift.core.domain.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "`user`")
public class UserEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;

    protected UserEntity() {
    }

    protected UserEntity(Long id, String name) {
        super(id);
        this.name = name;
    }

    public static UserEntity fromDomain(User user) {
        return new UserEntity(user.id(), user.name());
    }

    protected UserEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
