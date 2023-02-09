package fastcampus.toyproject2board.repository;

import fastcampus.toyproject2board.domain.NoticeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface NoticeCommentRepository extends JpaRepository<NoticeComment, Long> {
}
