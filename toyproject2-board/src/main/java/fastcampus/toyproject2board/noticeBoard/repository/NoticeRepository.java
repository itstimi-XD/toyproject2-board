package fastcampus.toyproject2board.noticeBoard.repository;

import fastcampus.toyproject2board.noticeBoard.domain.Notice;
import fastcampus.toyproject2board.noticeBoard.domain.QNotice;
import fastcampus.toyproject2board.noticeBoard.repository.querydsl.NoticeRepositoryCustom;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface NoticeRepository extends
        JpaRepository<Notice, Long>,
        NoticeRepositoryCustom,
        QuerydslPredicateExecutor<Notice>,
        QuerydslBinderCustomizer<QNotice> {

    Page<Notice> findByTitleContaining(String title, Pageable pageable);
    Page<Notice> findByContentContaining(String content, Pageable pageable);
    Page<Notice> findByUserAccount_UserIdContaining(String userId, Pageable pageable);
    Page<Notice> findByUserAccount_NicknameContaining(String nickname, Pageable pageable);
    Page<Notice> findByHashtag(String hashtag, Pageable pageable);

    void deleteByIdAndUserAccount_UserId(Long noticeId, String userid);

    @Override
    default void customize(QuerydslBindings bindings, QNotice root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy);
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }

}
