package fastcampus.toyproject2board.entity;

import fastcampus.toyproject2board.dto.MemberDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
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

    public static MemberEntity toMemberEntity(MemberDTO memberDTO) {
        return MemberEntity.builder()
                .memberId(memberDTO.getMemberId())
                .memberEmail(memberDTO.getMemberEmail())
                .memberPassword(memberDTO.getMemberPassword())
                .memberName(memberDTO.getMemberName())
                .build();
    }
}
