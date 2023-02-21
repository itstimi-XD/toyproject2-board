package fastcampus.toyproject2board.noticeBoard.dto;

import fastcampus.toyproject2board.noticeBoard.domain.Notice;
import fastcampus.toyproject2board.noticeBoard.domain.NoticeComment;
import fastcampus.toyproject2board.noticeBoard.domain.UserAccount;

import java.time.LocalDateTime;

public record NoticeCommentDto(
        Long id,
        Long noticeId,
        UserAccountDto userAccountDto,
        String content,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {

    public static NoticeCommentDto of(Long noticeId, UserAccountDto userAccountDto, String content) {
        return new NoticeCommentDto(null, noticeId, userAccountDto, content, null, null, null, null);
    }
    public static NoticeCommentDto of(Long id, Long noticeId, UserAccountDto userAccountDto, String content, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new NoticeCommentDto(id, noticeId, userAccountDto, content, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static NoticeCommentDto from(NoticeComment entity) {
        return new NoticeCommentDto(
                entity.getId(),
                entity.getNotice().getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

    public NoticeComment toEntity(Notice notice, UserAccount userAccount) {
        return NoticeComment.of(
                notice,
                userAccount,
                content
        );
    }

}
