package fastcampus.toyproject2board.noticeBoard.service;

import fastcampus.toyproject2board.noticeBoard.domain.Notice;
import fastcampus.toyproject2board.noticeBoard.domain.UserAccount;
import fastcampus.toyproject2board.noticeBoard.domain.constant.SearchType;
import fastcampus.toyproject2board.noticeBoard.dto.NoticeDto;
import fastcampus.toyproject2board.noticeBoard.dto.NoticeWithCommentsDto;
import fastcampus.toyproject2board.noticeBoard.repository.NoticeRepository;
import fastcampus.toyproject2board.noticeBoard.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    public Page<NoticeDto> searchNotices(SearchType searchType, String searchKeyword, Pageable pageable) {
        if (searchKeyword == null || searchKeyword.isBlank()) {
            return noticeRepository.findAll(pageable).map(NoticeDto::from);
        }

        return switch (searchType) {
            case TITLE -> noticeRepository.findByTitleContaining(searchKeyword, pageable).map(NoticeDto::from);
            case CONTENT -> noticeRepository.findByContentContaining(searchKeyword, pageable).map(NoticeDto::from);
            case ID -> noticeRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(NoticeDto::from);
            case NICKNAME -> noticeRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(NoticeDto::from);
            case HASHTAG -> noticeRepository.findByHashtag("#" + searchKeyword, pageable).map(NoticeDto::from);
        };
    }

    @Transactional(readOnly = true)
    public NoticeWithCommentsDto getNoticeWithComments(Long noticeId) {
        return noticeRepository.findById(noticeId)
                .map(NoticeWithCommentsDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - noticeId: " + noticeId));
    }

    @Transactional(readOnly = true)
    public NoticeDto getNotice(Long noticeId) {
        return noticeRepository.findById(noticeId)
                .map(NoticeDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - noticeId: " + noticeId));
    }

    public void saveNotice(NoticeDto dto) {
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());
        noticeRepository.save(dto.toEntity(userAccount));
    }

    public void updateNotice(Long noticeId, NoticeDto dto) {
        try {
            Notice notice = noticeRepository.getReferenceById(noticeId);
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());

            if (notice.getUserAccount().equals(userAccount)) {
                if (dto.title() != null) { notice.setTitle(dto.title()); }
                if (dto.content() != null) { notice.setContent(dto.content()); }
                notice.setHashtag(dto.hashtag());
            }
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 게시글을 수정하는데 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }
    }

    public void deleteNotice(long noticeId, String userId) {
        noticeRepository.deleteByIdAndUserAccount_UserId(noticeId, userId);
    }

    public long getNoticeCount() {
        return noticeRepository.count();
    }

    @Transactional(readOnly = true)
    public Page<NoticeDto> searchNoticesViaHashtag(String hashtag, Pageable pageable) {
        if (hashtag == null || hashtag.isBlank()) {
            return Page.empty(pageable);
        }

        return noticeRepository.findByHashtag(hashtag, pageable).map(NoticeDto::from);
    }

    public List<String> getHashtags() {
        return noticeRepository.findAllDistinctHashtags();
    }

}
