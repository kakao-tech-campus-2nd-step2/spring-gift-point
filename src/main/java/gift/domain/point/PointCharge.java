package gift.domain.point;

import gift.util.page.PageParam;
import java.time.LocalDateTime;

public class PointCharge {

    public PointCharge() {
    }

    public static class getList extends PageParam {

    }

    public static class CreatePointCharge {

        private Integer price;

        public CreatePointCharge() {
        }

        public CreatePointCharge(Integer price) {
            this.price = price;
        }

        public Integer getPrice() {
            return price;
        }
    }

    public static class PointChargeSimple {

        private Long id;
        private Integer point;

        public PointChargeSimple(Long id, Integer point) {
            this.id = id;
            this.point = point;
        }

        public Long getId() {
            return id;
        }

        public Integer getPoint() {
            return point;
        }
    }

    public static class PointChargeDetail {

        private Long id;
        private Integer point;
        private LocalDateTime transactionDate;

        public PointChargeDetail(Long id, Integer point, LocalDateTime transactionDate) {
            this.id = id;
            this.point = point;
            this.transactionDate = transactionDate;
        }

        public Long getId() {
            return id;
        }

        public Integer getPoint() {
            return point;
        }

        public LocalDateTime getTransactionDate() {
            return transactionDate;
        }
    }
}
