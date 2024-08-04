package gift.orderOption.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cash_receipt")
public class CashReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cash_receipt_type", nullable = false)
    private String cashReceiptType;

    @Column(name = "cash_receipt_number", nullable = false)
    private String cashReceiptNumber;

    public CashReceipt(String cashReceiptType, String cashReceiptNumber) {
        this.cashReceiptType = cashReceiptType;
        this.cashReceiptNumber = cashReceiptNumber;
    }
}
