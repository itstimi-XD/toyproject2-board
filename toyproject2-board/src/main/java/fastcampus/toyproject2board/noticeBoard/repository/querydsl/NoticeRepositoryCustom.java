package fastcampus.toyproject2board.noticeBoard.repository.querydsl;

import java.util.List;

public interface NoticeRepositoryCustom {
    List<String> findAllDistinctHashtags();
}
