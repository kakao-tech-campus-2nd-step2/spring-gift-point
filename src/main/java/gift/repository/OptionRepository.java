package gift.repository;

import gift.domain.Option;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OptionRepository extends JpaRepository<Option, Long> {
    Option save(Option option);

    Optional<Option> findById(Long id);

    void deleteById(Long id);
}
