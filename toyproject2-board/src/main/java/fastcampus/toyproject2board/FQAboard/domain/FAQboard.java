package fastcampus.toyproject2board.FQAboard.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FAQboard extends FAQBaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tno;

    @Column(length = 100,nullable = false)
    private String FAQtype;

    @Column(length = 250,nullable = false)
    private String title;

    @Column(length = 5000,nullable = false)
    private String content;

    @Column(length = 250,nullable = false)
    private String id;

    public void change(String FAQtype, String title,String content){
        this.FAQtype=FAQtype;
        this.title=title;
        this.content=content;
    }

}
