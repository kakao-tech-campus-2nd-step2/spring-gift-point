package gift.repository;

import gift.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OptionRepository extends JpaRepository<Option, Long> {
    Optional<Option> findById(int id);
    int searchQuantityById(int id);
    @Modifying
    @Query("UPDATE Option o SET o.quantity = :newQuantity WHERE o.id = :id")
    int updateQuantityById(@Param("id") int id, @Param("newQuantity") int newQuantity);}