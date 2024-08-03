package gift.repository;

import gift.domain.CashReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashReceiptRepository extends JpaRepository<CashReceipt, Long> {

}
