package gift.entity;

import jakarta.persistence.*;

@Entity
@Table
public class Point {

    @OneToOne
    Member member;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int point;


    public Point() {
    }

    public Point(Member member, int point) {
        this.member = member;
        this.point = point;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMemberId(Member member) {
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void subtractPoint(int point) {
        if (this.point < point) {
            throw new RuntimeException("Not enough point available");
        }
        this.point -= point;
    }

    public void addPoint(int point) {
        this.point += point;
    }
}
