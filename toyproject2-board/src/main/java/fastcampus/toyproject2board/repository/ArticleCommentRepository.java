package fastcampus.toyproject2board.repository;

import fastcampus.toyproject2board.freeboard.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
}
