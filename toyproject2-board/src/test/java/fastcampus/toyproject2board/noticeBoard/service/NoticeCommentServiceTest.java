package fastcampus.toyproject2board.noticeBoard.service;

import fastcampus.toyproject2board.noticeBoard.domain.Notice;
import fastcampus.toyproject2board.noticeBoard.domain.NoticeComment;
import fastcampus.toyproject2board.noticeBoard.domain.UserAccount;
import fastcampus.toyproject2board.noticeBoard.dto.NoticeCommentDto;
import fastcampus.toyproject2board.noticeBoard.dto.UserAccountDto;
import fastcampus.toyproject2board.noticeBoard.repository.NoticeCommentRepository;
import fastcampus.toyproject2board.noticeBoard.repository.NoticeRepository;
import fastcampus.toyproject2board.noticeBoard.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
class NoticeCommentServiceTest {

    @InjectMocks private NoticeCommentService sut;

    @Mock private NoticeRepository noticeRepository;
    @Mock private NoticeCommentRepository noticeCommentRepository;
    @Mock private UserAccountRepository userAccountRepository;

    @DisplayName("게시글 ID로 조회하면, 해당하는 댓글 리스트를 반환한다.")
    @Test
    void givenNoticeId_whenSearchingNoticeComments_thenReturnsNoticeComments() {
        // Given
        Long noticeId = 1L;
        NoticeComment expected = createNoticeComment("content");
        given(noticeCommentRepository.findByNotice_Id(noticeId)).willReturn(List.of(expected));

        // When
        List<NoticeCommentDto> actual = sut.searchNoticeComments(noticeId);

        // Then
        assertThat(actual)
                .hasSize(1)
                .first().hasFieldOrPropertyWithValue("content", expected.getContent());
        then(noticeCommentRepository).should().findByNotice_Id(noticeId);
    }

    @DisplayName("댓글 정보를 입력하면, 댓글을 저장한다.")
    @Test
    void givenNoticeCommentInfo_whenSavingNoticeComment_thenSavesNoticeComment() {
        // Given
        NoticeCommentDto dto = createNoticeCommentDto("댓글");
        given(noticeRepository.getReferenceById(dto.noticeId())).willReturn(createNotice());
        given(userAccountRepository.getReferenceById(dto.userAccountDto().userId())).willReturn(createUserAccount());
        given(noticeCommentRepository.save(any(NoticeComment.class))).willReturn(null);

        // When
        sut.saveNoticeComment(dto);

        // Then
        then(noticeRepository).should().getReferenceById(dto.noticeId());
        then(userAccountRepository).should().getReferenceById(dto.userAccountDto().userId());
        then(noticeCommentRepository).should().save(any(NoticeComment.class));
    }

    @DisplayName("댓글 저장을 시도했는데 맞는 게시글이 없으면, 경고 로그를 찍고 아무것도 안 한다.")
    @Test
    void givenNonexistentNotice_whenSavingNoticeComment_thenLogsSituationAndDoesNothing() {
        // Given
        NoticeCommentDto dto = createNoticeCommentDto("댓글");
        given(noticeRepository.getReferenceById(dto.noticeId())).willThrow(EntityNotFoundException.class);

        // When
        sut.saveNoticeComment(dto);

        // Then
        then(noticeRepository).should().getReferenceById(dto.noticeId());
        then(userAccountRepository).shouldHaveNoInteractions();
        then(noticeCommentRepository).shouldHaveNoInteractions();
    }

    @DisplayName("댓글 정보를 입력하면, 댓글을 수정한다.")
    @Test
    void givenNoticeCommentInfo_whenUpdatingNoticeComment_thenUpdatesNoticeComment() {
        // Given
        String oldContent = "content";
        String updatedContent = "댓글";
        NoticeComment noticeComment = createNoticeComment(oldContent);
        NoticeCommentDto dto = createNoticeCommentDto(updatedContent);
        given(noticeCommentRepository.getReferenceById(dto.id())).willReturn(noticeComment);

        // When
        sut.updateNoticeComment(dto);

        // Then
        assertThat(noticeComment.getContent())
                .isNotEqualTo(oldContent)
                .isEqualTo(updatedContent);
        then(noticeCommentRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("없는 댓글 정보를 수정하려고 하면, 경고 로그를 찍고 아무 것도 안 한다.")
    @Test
    void givenNonexistentNoticeComment_whenUpdatingNoticeComment_thenLogsWarningAndDoesNothing() {
        // Given
        NoticeCommentDto dto = createNoticeCommentDto("댓글");
        given(noticeCommentRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);

        // When
        sut.updateNoticeComment(dto);

        // Then
        then(noticeCommentRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("댓글 ID를 입력하면, 댓글을 삭제한다.")
    @Test
    void givenNoticeCommentId_whenDeletingNoticeComment_thenDeletesNoticeComment() {
        // Given
        Long noticeCommentId = 1L;
        String userId = "uno";
        willDoNothing().given(noticeCommentRepository).deleteByIdAndUserAccount_UserId(noticeCommentId, userId);

        // When
        sut.deleteNoticeComment(noticeCommentId, userId);

        // Then
        then(noticeCommentRepository).should().deleteByIdAndUserAccount_UserId(noticeCommentId, userId);
    }


    private NoticeCommentDto createNoticeCommentDto(String content) {
        return NoticeCommentDto.of(
                1L,
                1L,
                createUserAccountDto(),
                content,
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
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

    private NoticeComment createNoticeComment(String content) {
        return NoticeComment.of(
                Notice.of(createUserAccount(), "title", "content", "hashtag"),
                createUserAccount(),
                content
        );
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
        return Notice.of(
                createUserAccount(),
                "title",
                "content",
                "#java"
        );
    }

}
