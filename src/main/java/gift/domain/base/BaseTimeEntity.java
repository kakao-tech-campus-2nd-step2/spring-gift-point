package gift.domain.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected BaseTimeEntity() {
    }

    protected abstract static class Builder<T extends Builder<T>> {

        Long id;

        public T id(Long id) {
            this.id = id;
            return self();
        }

        protected abstract BaseTimeEntity build();

        protected abstract T self();
    }

    protected BaseTimeEntity(Builder<?> builder) {
        id = builder.id;
    }

    public Long getId() {
        return id;
    }
}
