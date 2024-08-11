    package gift.entity;

    import gift.repository.OptionsRepository;
    import gift.repository.ProductRepository;
    import jakarta.persistence.*;

    import java.util.NoSuchElementException;

    @Entity
    @Table(name = "orders")
    public class Order {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @ManyToOne
        @JoinColumn(name = "option_id", nullable = false)
        private Option option;

        @ManyToOne
        @JoinColumn(name = "product_id")
        private Product product;

        @Column(name = "quantity")
        private int quantity;

        @Column(name="timestamp", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
        private String timestamp;

        @Column(name = "message")
        private String message;

        protected Order() {}

        public Order(Option option, Product product, int quantity, String timestamp, String message) {
            this.option = option;
            this.product = product;
            this.quantity = quantity;
            this.timestamp = timestamp;
            this.message = message;
        }

        public int getId() {
            return id;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public String getMessage() {
            return message;
        }

        public Option getOption() {
            return option;
        }

        public int getQuantity() {
            return quantity;
        }

        public int getPoint() {
            int pointBound = 50000;
            var totalPrice = product.getPrice() * quantity;

            if(totalPrice >= pointBound) {
                return (int) (totalPrice - pointBound);
            }

            return 0;
        }
    }
