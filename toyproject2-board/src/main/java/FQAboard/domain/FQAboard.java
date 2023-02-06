package FQAboard.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class FQAboard extends FAQBaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(length = 100,nullable = false)
    private String FAQtype;

    @Column(length = 250,nullable = false)
    private String title;

    @Column(length = 5000)
    private String content;

    @Column(length = 250,nullable = false)
    private String id;


}
