package fastcampus.toyproject2board.noticeBoard.repository.querydsl;

import fastcampus.toyproject2board.noticeBoard.domain.Notice;
import fastcampus.toyproject2board.noticeBoard.domain.QNotice;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class NoticeRepositoryCustomImpl extends QuerydslRepositorySupport implements NoticeRepositoryCustom {

    public NoticeRepositoryCustomImpl() {
        super(Notice.class);
    }

    @Override
    public List<String> findAllDistinctHashtags() {
        QNotice notice = QNotice.notice;

        return from(notice)
                .distinct()
                .select(notice.hashtag)
                .where(notice.hashtag.isNotNull())
                .fetch();
    }

}
