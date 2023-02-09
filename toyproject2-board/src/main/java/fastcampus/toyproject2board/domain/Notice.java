package fastcampus.toyproject2board.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),

})

@Entity
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column(nullable = false)                   private String title; // 제목
    @Setter @Column(nullable = false, length = 10000)   private String content; // 내용

    @CreatedDate        @Column(nullable = false)               private LocalDateTime createdAt; // 생성일시
    @CreatedBy          @Column(nullable = false, length = 100) private String createdBy; // 생성자
    @LastModifiedDate   @Column(nullable = false)               private LocalDateTime modifiedAt; //수정일시
    @LastModifiedBy     @Column(nullable = false, length = 100) private String modifiedBy; // 수정자

    protected Notice() {}

    private Notice(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static Notice of(String title, String content){
        return new Notice(title,content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notice notice = (Notice) o;
        return id != null && id.equals(notice.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
