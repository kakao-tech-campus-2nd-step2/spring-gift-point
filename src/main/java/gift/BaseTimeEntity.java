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
    @Column(name = "create_date", updatable = false, nullable = false)
    private LocalDateTime registrationDate;

    @LastModifiedDate
    @Column(name = "modification_date", nullable = false)
    private LocalDateTime modificationDate;

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public LocalDateTime getModificationDate() {
        return modificationDate;
    }
}