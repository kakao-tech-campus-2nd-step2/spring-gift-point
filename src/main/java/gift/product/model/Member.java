package gift.product.model;

import jakarta.persistence.*;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn
    private SnsMember snsMember;

    @Column(nullable = false)
    private int point;

    public Member() {}

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
        this.point = 0;
    }

    public Member(SnsMember snsMember) {
        this.email = "";
        this.password = "";
        this.snsMember = snsMember;
        this.point = 0;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public SnsMember getSnsMember() {
        return snsMember;
    }

    public int getPoint() {
        return point;
    }

    public void addPoint(int point) {
        this.point += point;
    }

    public void subtractPoint(Order order) {
        int orderAmount = order.getOption().getProduct().getPrice() * order.getQuantity();
        int oneTenth = (int) (orderAmount * 0.1);
        if(oneTenth >= point) {
            point -= oneTenth;
            return;
        }
        point = 0;
    }
}
