package fastcampus.toyproject2board.noticeBoard.repository;

import fastcampus.toyproject2board.noticeBoard.domain.Notice;
import fastcampus.toyproject2board.noticeBoard.domain.UserAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA 연결 테스트")
@Import(JpaRepositoryTest.TestJpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

    private final NoticeRepository noticeRepository;
    private final NoticeCommentRepository noticeCommentRepository;
    private final UserAccountRepository userAccountRepository;

    public JpaRepositoryTest(
            @Autowired NoticeRepository noticeRepository,
            @Autowired NoticeCommentRepository noticeCommentRepository,
            @Autowired UserAccountRepository userAccountRepository
    ) {
        this.noticeRepository = noticeRepository;
        this.noticeCommentRepository = noticeCommentRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @DisplayName("select 테스트")
    @Test
    void givenTestData_whenSelecting_thenWorksFine() {
        // Given

        // When
        List<Notice> notices = noticeRepository.findAll();

        // Then
        assertThat(notices)
                .isNotNull()
                .hasSize(123);
    }

    @DisplayName("insert 테스트")
    @Test
    void givenTestData_whenInserting_thenWorksFine() {
        // Given
        long previousCount = noticeRepository.count();
        UserAccount userAccount = userAccountRepository.save(UserAccount.of("newUno", "pw", null, null, null));
        Notice notice = Notice.of(userAccount, "new notice", "new content", "#spring");

        // When
        noticeRepository.save(notice);

        // Then
        assertThat(noticeRepository.count()).isEqualTo(previousCount + 1);
    }

    @DisplayName("update 테스트")
    @Test
    void givenTestData_whenUpdating_thenWorksFine() {
        // Given
        Notice notice = noticeRepository.findById(1L).orElseThrow();
        String updatedHashtag = "#springboot";
        notice.setHashtag(updatedHashtag);

        // When
        Notice savedNotice = noticeRepository.saveAndFlush(notice);

        // Then
        assertThat(savedNotice).hasFieldOrPropertyWithValue("hashtag", updatedHashtag);
    }

    @DisplayName("delete 테스트")
    @Test
    void givenTestData_whenDeleting_thenWorksFine() {
        // Given
        Notice notice = noticeRepository.findById(1L).orElseThrow();
        long previousNoticeCount = noticeRepository.count();
        long previousNoticeCommentCount = noticeCommentRepository.count();
        int deletedCommentsSize = notice.getNoticeComments().size();

        // When
        noticeRepository.delete(notice);

        // Then
        assertThat(noticeRepository.count()).isEqualTo(previousNoticeCount - 1);
        assertThat(noticeCommentRepository.count()).isEqualTo(previousNoticeCommentCount - deletedCommentsSize);
    }

    @EnableJpaAuditing
    @TestConfiguration
    public static class TestJpaConfig {
        @Bean
        public AuditorAware<String> auditorAware() {
            return () -> Optional.of("uno");
        }
    }

}
