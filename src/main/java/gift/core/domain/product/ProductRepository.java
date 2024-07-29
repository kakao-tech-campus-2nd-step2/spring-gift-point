package gift.core.domain.product;

import gift.core.PagedDto;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Optional<Product> findById(Long id);

    boolean exists(Long id);

    void save(@Nonnull Product product);

    long size();

    List<Product> findAll();

    PagedDto<Product> findAll(Pageable pageable);

    void remove(Long id);

}
