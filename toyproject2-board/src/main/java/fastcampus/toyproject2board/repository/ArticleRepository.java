package fastcampus.toyproject2board.repository;

import fastcampus.toyproject2board.freeboard.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
