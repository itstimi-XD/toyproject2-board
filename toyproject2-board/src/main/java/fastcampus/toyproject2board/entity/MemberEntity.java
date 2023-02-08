package fastcampus.toyproject2board.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "member_table")
public class MemberEntity {
    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String memberId;
    @Column(unique = true)
    private String memberEmail;
    @Column
    private String memberPassword;
    @Column
    private String memberName;

}
