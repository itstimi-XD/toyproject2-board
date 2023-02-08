package fastcampus.toyproject2board.dto;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private Long id;
    private String memberId;
    private String memberEmail;
    private String memberPassword;
    private String memberName;
}
