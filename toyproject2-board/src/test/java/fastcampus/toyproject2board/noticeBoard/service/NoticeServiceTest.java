package fastcampus.toyproject2board.noticeBoard.service;

import fastcampus.toyproject2board.noticeBoard.domain.Notice;
import fastcampus.toyproject2board.noticeBoard.domain.UserAccount;
import fastcampus.toyproject2board.noticeBoard.domain.constant.SearchType;
import fastcampus.toyproject2board.noticeBoard.dto.NoticeDto;
import fastcampus.toyproject2board.noticeBoard.dto.NoticeWithCommentsDto;
import fastcampus.toyproject2board.noticeBoard.dto.UserAccountDto;
import fastcampus.toyproject2board.noticeBoard.repository.NoticeRepository;
import fastcampus.toyproject2board.noticeBoard.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class NoticeServiceTest {

    @InjectMocks private NoticeService sut;

    @Mock private NoticeRepository noticeRepository;
    @Mock private UserAccountRepository userAccountRepository;

    @DisplayName("검색어 없이 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenNoSearchParameters_whenSearchingNotices_thenReturnsNoticePage() {
        // Given
        Pageable pageable = Pageable.ofSize(20);
        given(noticeRepository.findAll(pageable)).willReturn(Page.empty());

        // When
        Page<NoticeDto> notices = sut.searchNotices(null, null, pageable);

        // Then
        assertThat(notices).isEmpty();
        then(noticeRepository).should().findAll(pageable);
    }

    @DisplayName("검색어와 함께 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenSearchParameters_whenSearchingNotices_thenReturnsNoticePage() {
        // Given
        SearchType searchType = SearchType.TITLE;
        String searchKeyword = "title";
        Pageable pageable = Pageable.ofSize(20);
        given(noticeRepository.findByTitleContaining(searchKeyword, pageable)).willReturn(Page.empty());

        // When
        Page<NoticeDto> notices = sut.searchNotices(searchType, searchKeyword, pageable);

        // Then
        assertThat(notices).isEmpty();
        then(noticeRepository).should().findByTitleContaining(searchKeyword, pageable);
    }

    @DisplayName("검색어 없이 게시글을 해시태그 검색하면, 빈 페이지를 반환한다.")
    @Test
    void givenNoSearchParameters_whenSearchingNoticesViaHashtag_thenReturnsEmptyPage() {
        // Given
        Pageable pageable = Pageable.ofSize(20);

        // When
        Page<NoticeDto> notices = sut.searchNoticesViaHashtag(null, pageable);

        // Then
        assertThat(notices).isEqualTo(Page.empty(pageable));
        then(noticeRepository).shouldHaveNoInteractions();
    }

    @DisplayName("게시글을 해시태그 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenHashtag_whenSearchingNoticesViaHashtag_thenReturnsNoticesPage() {
        // Given
        String hashtag = "#java";
        Pageable pageable = Pageable.ofSize(20);
        given(noticeRepository.findByHashtag(hashtag, pageable)).willReturn(Page.empty(pageable));

        // When
        Page<NoticeDto> notices = sut.searchNoticesViaHashtag(hashtag, pageable);

        // Then
        assertThat(notices).isEqualTo(Page.empty(pageable));
        then(noticeRepository).should().findByHashtag(hashtag, pageable);
    }

    @DisplayName("게시글 ID로 조회하면, 댓글 달긴 게시글을 반환한다.")
    @Test
    void givenNoticeId_whenSearchingNoticeWithComments_thenReturnsNoticeWithComments() {
        // Given
        Long noticeId = 1L;
        Notice notice = createNotice();
        given(noticeRepository.findById(noticeId)).willReturn(Optional.of(notice));

        // When
        NoticeWithCommentsDto dto = sut.getNoticeWithComments(noticeId);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("title", notice.getTitle())
                .hasFieldOrPropertyWithValue("content", notice.getContent())
                .hasFieldOrPropertyWithValue("hashtag", notice.getHashtag());
        then(noticeRepository).should().findById(noticeId);
    }

    @DisplayName("댓글 달린 게시글이 없으면, 예외를 던진다.")
    @Test
    void givenNonexistentNoticeId_whenSearchingNoticeWithComments_thenThrowsException() {
        // Given
        Long noticeId = 0L;
        given(noticeRepository.findById(noticeId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> sut.getNoticeWithComments(noticeId));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("게시글이 없습니다 - noticeId: " + noticeId);
        then(noticeRepository).should().findById(noticeId);
    }

    @DisplayName("게시글을 조회하면, 게시글을 반환한다.")
    @Test
    void givenNoticeId_whenSearchingNotice_thenReturnsNotice() {
        // Given
        Long noticeId = 1L;
        Notice notice = createNotice();
        given(noticeRepository.findById(noticeId)).willReturn(Optional.of(notice));

        // When
        NoticeDto dto = sut.getNotice(noticeId);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("title", notice.getTitle())
                .hasFieldOrPropertyWithValue("content", notice.getContent())
                .hasFieldOrPropertyWithValue("hashtag", notice.getHashtag());
        then(noticeRepository).should().findById(noticeId);
    }

    @DisplayName("게시글이 없으면, 예외를 던진다.")
    @Test
    void givenNonexistentNoticeId_whenSearchingNotice_thenThrowsException() {
        // Given
        Long noticeId = 0L;
        given(noticeRepository.findById(noticeId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> sut.getNotice(noticeId));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("게시글이 없습니다 - noticeId: " + noticeId);
        then(noticeRepository).should().findById(noticeId);
    }

    @DisplayName("게시글 정보를 입력하면, 게시글을 생성한다.")
    @Test
    void givenNoticeInfo_whenSavingNotice_thenSavesNotice() {
        // Given
        NoticeDto dto = createNoticeDto();
        given(userAccountRepository.getReferenceById(dto.userAccountDto().userId())).willReturn(createUserAccount());
        given(noticeRepository.save(any(Notice.class))).willReturn(createNotice());

        // When
        sut.saveNotice(dto);

        // Then
        then(userAccountRepository).should().getReferenceById(dto.userAccountDto().userId());
        then(noticeRepository).should().save(any(Notice.class));
    }

    @DisplayName("게시글의 수정 정보를 입력하면, 게시글을 수정한다.")
    @Test
    void givenModifiedNoticeInfo_whenUpdatingNotice_thenUpdatesNotice() {
        // Given
        Notice notice = createNotice();
        NoticeDto dto = createNoticeDto("새 타이틀", "새 내용", "#springboot");
        given(noticeRepository.getReferenceById(dto.id())).willReturn(notice);
        given(userAccountRepository.getReferenceById(dto.userAccountDto().userId())).willReturn(dto.userAccountDto().toEntity());

        // When
        sut.updateNotice(dto.id(), dto);

        // Then
        assertThat(notice)
                .hasFieldOrPropertyWithValue("title", dto.title())
                .hasFieldOrPropertyWithValue("content", dto.content())
                .hasFieldOrPropertyWithValue("hashtag", dto.hashtag());
        then(noticeRepository).should().getReferenceById(dto.id());
        then(userAccountRepository).should().getReferenceById(dto.userAccountDto().userId());
    }

    @DisplayName("없는 게시글의 수정 정보를 입력하면, 경고 로그를 찍고 아무 것도 하지 않는다.")
    @Test
    void givenNonexistentNoticeInfo_whenUpdatingNotice_thenLogsWarningAndDoesNothing() {
        // Given
        NoticeDto dto = createNoticeDto("새 타이틀", "새 내용", "#springboot");
        given(noticeRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);

        // When
        sut.updateNotice(dto.id(), dto);

        // Then
        then(noticeRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다")
    @Test
    void givenNoticeId_whenDeletingNotice_thenDeletesNotice() {
        // Given
        Long noticeId = 1L;
        String userId = "uno";
        willDoNothing().given(noticeRepository).deleteByIdAndUserAccount_UserId(noticeId, userId);

        // When
        sut.deleteNotice(1L, userId);

        // Then
        then(noticeRepository).should().deleteByIdAndUserAccount_UserId(noticeId, userId);
    }

    @DisplayName("게시글 수를 조회하면, 게시글 수를 반환한다")
    @Test
    void givenNothing_whenCountingNotices_thenReturnsNoticeCount() {
        // Given
        long expected = 0L;
        given(noticeRepository.count()).willReturn(expected);

        // When
        long actual = sut.getNoticeCount();

        // Then
        assertThat(actual).isEqualTo(expected);
        then(noticeRepository).should().count();
    }

    @DisplayName("해시태그를 조회하면, 유니크 해시태그 리스트를 반환한다")
    @Test
    void givenNothing_whenCalling_thenReturnsHashtags() {
        // Given
        List<String> expectedHashtags = List.of("#java", "#spring", "#boot");
        given(noticeRepository.findAllDistinctHashtags()).willReturn(expectedHashtags);

        // When
        List<String> actualHashtags = sut.getHashtags();

        // Then
        assertThat(actualHashtags).isEqualTo(expectedHashtags);
        then(noticeRepository).should().findAllDistinctHashtags();
    }


    private UserAccount createUserAccount() {
        return UserAccount.of(
                "uno",
                "password",
                "uno@email.com",
                "Uno",
                null
        );
    }

    private Notice createNotice() {
        Notice notice = Notice.of(
                createUserAccount(),
                "title",
                "content",
                "#java"
        );
        ReflectionTestUtils.setField(notice, "id", 1L);

        return notice;
    }

    private NoticeDto createNoticeDto() {
        return createNoticeDto("title", "content", "#java");
    }

    private NoticeDto createNoticeDto(String title, String content, String hashtag) {
        return NoticeDto.of(
                1L,
                createUserAccountDto(),
                title,
                content,
                hashtag,
                LocalDateTime.now(),
                "Uno",
                LocalDateTime.now(),
                "Uno");
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "uno",
                "password",
                "uno@mail.com",
                "Uno",
                "This is memo",
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }

}
