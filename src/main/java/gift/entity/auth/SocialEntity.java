package gift.entity.auth;

import gift.entity.enums.SocialType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "SocialLogin")
@EntityListeners(AuditingEntityListener.class)
public class SocialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long socialId;

    @NotNull
    private SocialType type;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "User_id")
    private UserEntity userEntity;

    public SocialEntity() {
    }

    public SocialEntity(Long socialId, SocialType type, UserEntity userEntity) {
        this.socialId = socialId;
        this.type = type;
        this.userEntity = userEntity;
    }

    public Long getId() {
        return id;
    }

    public Long getSocialId() {
        return socialId;
    }

    public SocialType getType() {
        return type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }
}
