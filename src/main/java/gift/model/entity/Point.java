package gift.model.entity;

import jakarta.persistence.*;

@Entity
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_point_member"), nullable = false)
    private Member member;

    @Column(name = "point", columnDefinition = "integer", nullable = false)
    private Long point;

    protected Point() {
    }

    public Point(Member member, long point) {
        this.member = member;
        this.point = point;
    }

    public Point(long id, Member member, long point) {
        this.id = id;
        this.member = member;
        this.point = point;
    }

    public Point updatePoint(long points){
        this.point = this.point + points;
        return this;
    }

    public long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public long getPoint() {
        return point;
    }
}
