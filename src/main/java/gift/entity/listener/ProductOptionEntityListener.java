package gift.entity.listener;

import gift.entity.ProductOptionEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

public class ProductOptionEntityListener {

    @PrePersist
    @PreUpdate
    public void prePersistOrUpdate(ProductOptionEntity option) {
        if (option.getProductEntity() != null) {
            if (!option.getProductEntity().getProductOptions().contains(option)) {
                option.getProductEntity().getProductOptions().add(option);
            }
        }
    }

    @PreRemove
    public void preRemove(ProductOptionEntity option) {
        if (option.getProductEntity() != null) {
            option.getProductEntity().getProductOptions().remove(option);
        }
    }
}
