package fastcampus.toyproject2board.noticeBoard.repository;

import fastcampus.toyproject2board.noticeBoard.domain.NoticeComment;
import fastcampus.toyproject2board.noticeBoard.domain.QNoticeComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface NoticeCommentRepository extends
        JpaRepository<NoticeComment, Long>,
        QuerydslPredicateExecutor<NoticeComment>,
        QuerydslBinderCustomizer<QNoticeComment> {

    List<NoticeComment> findByNotice_Id(Long noticeId);
    void deleteByIdAndUserAccount_UserId(Long noticeCommentId, String userId);

    @Override
    default void customize(QuerydslBindings bindings, QNoticeComment root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.content, root.createdAt, root.createdBy);
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }

}
