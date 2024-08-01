    package gift.entity;

    import jakarta.persistence.*;

    @Entity
    @Table(name = "orders")
    public class Order {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @ManyToOne
        @JoinColumn(name = "option_id", nullable = false)
        private Option option;

        @Column(name = "quantity")
        private int quantity;

        @Column(name="timestamp", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
        private String timestamp;

        @Column(name = "message")
        private String message;

        protected Order() {}

        public Order(Option option, int quantity, String timestamp, String message) {
            this.option = option;
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
    }
