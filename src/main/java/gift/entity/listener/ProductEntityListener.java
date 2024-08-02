package gift.entity.listener;

import gift.entity.ProductEntity;
import gift.entity.ProductOptionEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

public class ProductEntityListener {

    @PrePersist
    @PreUpdate
    public void prePersistOrUpdate(ProductEntity product) {
        if (product.getProductOptions() != null) {
            for (ProductOptionEntity option : product.getProductOptions()) {
                option.setProductEntity(product);
            }
        }
    }

    @PreRemove
    public void preRemove(ProductEntity product) {
        if (product.getProductOptions() != null) {
            for (ProductOptionEntity option : product.getProductOptions()) {
                option.setProductEntity(null);
            }
        }
    }
}
