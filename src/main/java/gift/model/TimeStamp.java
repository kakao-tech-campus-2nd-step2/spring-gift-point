package gift.model;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


    @MappedSuperclass
    @EntityListeners(AuditingEntityListener.class)
    public abstract class TimeStamp {
        @CreatedDate
        private LocalDateTime createdDate;

        @LastModifiedDate
        private LocalDateTime updatedDate;
    }

