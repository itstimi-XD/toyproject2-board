package fastcampus.toyproject2board.repository;

import fastcampus.toyproject2board.config.JpaConfig;
import fastcampus.toyproject2board.domain.Notice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

    private final NoticeRepository noticeRepository;
    private final NoticeCommentRepository noticeCommentRepository;

    JpaRepositoryTest(
            @Autowired NoticeRepository noticeRepository,
            @Autowired NoticeCommentRepository noticeCommentRepository
    ) {
        this.noticeRepository = noticeRepository;
        this.noticeCommentRepository = noticeCommentRepository;
    }


    @Test
    void select_테스트(){
        List<Notice> notices = noticeRepository.findAll();

        assertThat(notices).isNotNull().hasSize(0);
    }

    @Test
    void insert_테스트(){
        long previousCount = noticeRepository.count();

        Notice savedNotice =noticeRepository.save(Notice.of("new title", "new content"));

        assertThat(noticeRepository.count()).isEqualTo(previousCount +1);
    }

    @Test
    void Updating_테스트(){
        noticeRepository.save(Notice.of("new title", "new content"));
        Notice notice = noticeRepository.findById(1L).orElseThrow();
        String updatedTitle = "updated title";
        notice.setTitle(updatedTitle);

        Notice savedNotice = noticeRepository.saveAndFlush(notice);

        assertThat(savedNotice).hasFieldOrPropertyWithValue("title", updatedTitle);
    }

    @Test
    void Delete_테스트(){
        noticeRepository.save(Notice.of("new title", "new content"));

        Notice notice = noticeRepository.findById(1L).orElseThrow();
        long previousNoticeCount = noticeRepository.count();
        long previousNoticeCommentCount = noticeCommentRepository.count();
        int deletedCommentsSize = notice.getNoticeComments().size();

        noticeRepository.delete(notice);

        assertThat(noticeRepository.count()).isEqualTo(previousNoticeCount - 1);
        assertThat(noticeCommentRepository.count()).isEqualTo(previousNoticeCommentCount - deletedCommentsSize);

    }

}