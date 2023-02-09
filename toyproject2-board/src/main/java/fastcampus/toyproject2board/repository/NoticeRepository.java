package fastcampus.toyproject2board.repository;

import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import fastcampus.toyproject2board.domain.Notice;
import fastcampus.toyproject2board.domain.QNotice;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource

public interface NoticeRepository extends
        JpaRepository<Notice, Long>,
        QuerydslPredicateExecutor<Notice>,
        QuerydslBinderCustomizer<QNotice>
{
    @Override
    default void customize(QuerydslBindings bindings, QNotice root){
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.title, root.createdAt, root.createdBy);
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }

}