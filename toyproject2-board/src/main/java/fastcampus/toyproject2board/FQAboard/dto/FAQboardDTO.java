package fastcampus.toyproject2board.FQAboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FAQboardDTO {

    private Long tno;

    private String FAQtype;

    private String title;

    private String content;

    private String id;

    private LocalDate regDate;

    private LocalDateTime modDate;
}
