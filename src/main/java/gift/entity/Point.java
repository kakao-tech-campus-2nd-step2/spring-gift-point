package gift.entity;

import gift.exception.CustomException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int balance;

    @OneToOne
    @JoinColumn
    private Member member;

    public Point(int balance, Member member) {
        this.balance = balance;
        this.member = member;
    }
    public void addPoints(int points) {
        this.balance += points;
    }
    public void deductPoints(int points) {
        if(this.balance >= points) {
            this.balance -= points;
        } else {
            throw new CustomException.GenericException("Insufficient points");
        }
    }
}
