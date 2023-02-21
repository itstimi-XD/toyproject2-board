package fastcampus.toyproject2board.noticeBoard.dto;

import fastcampus.toyproject2board.noticeBoard.domain.Notice;
import fastcampus.toyproject2board.noticeBoard.domain.UserAccount;

import java.time.LocalDateTime;

public record NoticeDto(
        Long id,
        UserAccountDto userAccountDto,
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {

    public static NoticeDto of(UserAccountDto userAccountDto, String title, String content, String hashtag) {
        return new NoticeDto(null, userAccountDto, title, content, hashtag, null, null, null, null);
    }

    public static NoticeDto of(Long id, UserAccountDto userAccountDto, String title, String content, String hashtag, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new NoticeDto(id, userAccountDto, title, content, hashtag, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static NoticeDto from(Notice entity) {
        return new NoticeDto(
                entity.getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getTitle(),
                entity.getContent(),
                entity.getHashtag(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

    public Notice toEntity(UserAccount userAccount) {
        return Notice.of(
                userAccount,
                title,
                content,
                hashtag
        );
    }

}
