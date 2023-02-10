package fastcampus.toyproject2board.repository;

import fastcampus.toyproject2board.freeboard.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
