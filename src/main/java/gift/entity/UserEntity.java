package gift.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "Users")
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column
    private String email;

    @NotNull
    @Column
    private String password;

    //  0은 존재, 1은 삭제
    @NotNull
    @Column
    private Integer isDelete;

    @Column
    private Integer point;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY,
        cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishEntity> wish;

    @JsonIgnore
    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY,
        cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SocialEntity> socials;

    @JsonIgnore
    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY,
        cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOrderEntity> orders;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
        cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PointChargeEntity> pointCharge;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
        cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PointPaymentEntity> pointPayment;

    public UserEntity() {
    }

    public UserEntity(String email, String password) {
        this.email = email;
        this.password = password;
        this.isDelete = 0;
        this.point=0;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<WishEntity> getWish() {
        return wish;
    }

    public Integer getPoint() {
        return point;
    }

    public List<SocialEntity> getSocials() {
        return socials;
    }

    public List<ProductOrderEntity> getOrders() {
        return orders;
    }
}
