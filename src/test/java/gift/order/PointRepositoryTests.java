package gift.order;

import gift.core.domain.order.exception.PointNotEnoughException;
import gift.core.exception.validation.InvalidArgumentException;
import gift.order.infrastructure.persistence.point.JpaPointRepository;
import gift.order.infrastructure.persistence.point.PointEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PointRepositoryTests {
    @Autowired
    private JpaPointRepository jpaPointRepository;

    @Test
    public void savePoint() {
        PointEntity point = PointEntity.of(0L, 100L);

        point = jpaPointRepository.save(point);

        assertThat(jpaPointRepository.findById(point.getUserId())).isPresent();
        assertThat(jpaPointRepository.findById(point.getUserId()).get()).isEqualTo(point);
    }

    @Test
    @Transactional
    public void subtractPoint() {
        PointEntity point = jpaPointRepository.save(PointEntity.of(0L, 100L));

        point.subtractPoint(50L);

        assertThat(jpaPointRepository.findById(point.getUserId()).get().getPoint()).isEqualTo(50L);
    }

    @Test
    @Transactional
    public void subtractPointNotEnough() {
        PointEntity point = jpaPointRepository.save(PointEntity.of(0L, 100L));
        assertThatThrownBy(() -> point.subtractPoint(150L))
                .isInstanceOf(PointNotEnoughException.class);
    }

    @Test
    @Transactional
    public void subtractNegativePoint() {
        PointEntity point = jpaPointRepository.save(PointEntity.of(0L, 100L));
        assertThatThrownBy(() -> point.subtractPoint(-50L))
                .isInstanceOf(InvalidArgumentException.class);
    }

    @Test
    @Transactional
    public void addPoint() {
        PointEntity point = jpaPointRepository.save(PointEntity.of(0L, 100L));

        point.addPoint(50L);

        assertThat(jpaPointRepository.findById(point.getUserId()).get().getPoint()).isEqualTo(150L);
    }

    @Test
    @Transactional
    public void addNegativePoint() {
        PointEntity point = jpaPointRepository.save(PointEntity.of(0L, 100L));
        assertThatThrownBy(() -> point.addPoint(-50L))
                .isInstanceOf(InvalidArgumentException.class);
    }
}
