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
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),

})
@Entity
public class NoticeComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter @ManyToOne(optional = false)            private Notice notice; // 게시글 (ID)
    @Setter @Column(nullable = false, length = 500) private String content; // 내용

    @CreatedDate        @Column(nullable = false)               private LocalDateTime createdAt; // 생성일시
    @CreatedBy          @Column(nullable = false, length = 100) private String createdBy; // 생성자
    @LastModifiedDate   @Column(nullable = false)               private LocalDateTime modifiedAt; //수정일시
    @LastModifiedBy     @Column(nullable = false, length = 100) private String modifiedBy; // 수정자

    protected NoticeComment() {}

    private NoticeComment(Notice notice, String content) {
        this.notice = notice;
        this.content = content;
    }

    public static NoticeComment of(Notice notice, String content){
        return new NoticeComment(notice,content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoticeComment that = (NoticeComment) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
