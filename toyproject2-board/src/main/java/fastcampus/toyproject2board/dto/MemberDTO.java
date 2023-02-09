package fastcampus.toyproject2board.dto;

import fastcampus.toyproject2board.entity.MemberEntity;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private Long id;
    private String memberId;
    private String memberEmail;
    private String memberPassword;
    private String memberName;

    public static MemberDTO toMemberDTO(MemberEntity memberEntity) {
        return MemberDTO.builder()
                .memberId(memberEntity.getMemberId())
                .memberEmail(memberEntity.getMemberEmail())
                .memberPassword(memberEntity.getMemberPassword())
                .memberName(memberEntity.getMemberName())
                .build();

    }
}
