package gift.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.UniqueElements;

@Entity
@Table
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private int point;


    private Long memberId;


    public Point() {
    }

    public Point(Member member, int point){
        this.memberId = member.getId();
        this.point = point;
    }

    public Point(Long memberId, int point){
        this.memberId = memberId;
        this.point = point;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getPoint() {
        return point;
    }

    public void subtractPoint(int point) {
        if (this.point < point) {
            throw new RuntimeException("Not enough point available");
        }
        this.point -= point;
    }
}
