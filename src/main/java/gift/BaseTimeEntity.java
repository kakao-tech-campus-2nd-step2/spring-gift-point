package gift;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(name = "registration_date", updatable = false, nullable = false)
    private LocalDateTime registrationDate;

    @LastModifiedDate
    @Column(name = "modification_date", nullable = false)
    private LocalDateTime modificationDate;

    @Column(name = "deletion_date")
    private LocalDateTime deletionDate = null;

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    public LocalDateTime getDeletionDate() {
        return deletionDate;
    }
}