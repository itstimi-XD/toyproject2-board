package fastcampus.toyproject2board.noticeBoard.service;

import fastcampus.toyproject2board.noticeBoard.domain.Notice;
import fastcampus.toyproject2board.noticeBoard.domain.NoticeComment;
import fastcampus.toyproject2board.noticeBoard.domain.UserAccount;
import fastcampus.toyproject2board.noticeBoard.dto.NoticeCommentDto;
import fastcampus.toyproject2board.noticeBoard.repository.NoticeCommentRepository;
import fastcampus.toyproject2board.noticeBoard.repository.NoticeRepository;
import fastcampus.toyproject2board.noticeBoard.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class NoticeCommentService {

    private final NoticeRepository noticeRepository;
    private final NoticeCommentRepository noticeCommentRepository;
    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    public List<NoticeCommentDto> searchNoticeComments(Long noticeId) {
        return noticeCommentRepository.findByNotice_Id(noticeId)
                .stream()
                .map(NoticeCommentDto::from)
                .toList();
    }

    public void saveNoticeComment(NoticeCommentDto dto) {
        try {
            Notice notice = noticeRepository.getReferenceById(dto.noticeId());
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());
            noticeCommentRepository.save(dto.toEntity(notice, userAccount));
        } catch (EntityNotFoundException e) {
            log.warn("댓글 저장 실패. 댓글 작성에 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }
    }

    public void updateNoticeComment(NoticeCommentDto dto) {
        try {
            NoticeComment noticeComment = noticeCommentRepository.getReferenceById(dto.id());
            if (dto.content() != null) { noticeComment.setContent(dto.content()); }
        } catch (EntityNotFoundException e) {
            log.warn("댓글 업데이트 실패. 댓글을 찾을 수 없습니다 - dto: {}", dto);
        }
    }

    public void deleteNoticeComment(Long noticeCommentId, String userId) {
        noticeCommentRepository.deleteByIdAndUserAccount_UserId(noticeCommentId, userId);
    }

}
