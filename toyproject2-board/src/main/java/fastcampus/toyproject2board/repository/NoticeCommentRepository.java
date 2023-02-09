package fastcampus.toyproject2board.repository;

import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import fastcampus.toyproject2board.domain.Notice;
import fastcampus.toyproject2board.domain.NoticeComment;
import fastcampus.toyproject2board.domain.QNotice;
import fastcampus.toyproject2board.domain.QNoticeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface NoticeCommentRepository extends
        JpaRepository<NoticeComment, Long>,
        QuerydslPredicateExecutor<NoticeComment>,
        QuerydslBinderCustomizer<QNoticeComment>
{
    @Override
    default void customize(QuerydslBindings bindings, QNoticeComment root){
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.createdAt, root.createdBy);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }
}
