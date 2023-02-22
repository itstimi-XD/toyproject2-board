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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FAQboardDTO {

    private Long tno;
    @NotEmpty
    private String FAQtype;
    @NotEmpty
    @Size(min = 2, max = 50)
    private String title;
    @NotEmpty
    @Size(min = 10, max = 500)
    private String content;
    @NotEmpty
    private String id;

    private LocalDate regDate;

    private LocalDateTime modDate;
}
