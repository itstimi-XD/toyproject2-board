package fastcampus.toyproject2board.repository;

import fastcampus.toyproject2board.domain.NoticeComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeCommentRepository extends JpaRepository<NoticeComment, Long> {
}
