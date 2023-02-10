package fastcampus.toyproject2board.noticeBoard.dto;

import fastcampus.toyproject2board.noticeBoard.domain.Notice;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record NoticeWithCommentsDto(
        Long id,
        UserAccountDto userAccountDto,
        Set<NoticeCommentDto> noticeCommentDtos,
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {
    public static NoticeWithCommentsDto of(Long id, UserAccountDto userAccountDto, Set<NoticeCommentDto> noticeCommentDtos, String title, String content, String hashtag, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new NoticeWithCommentsDto(id, userAccountDto, noticeCommentDtos, title, content, hashtag, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static NoticeWithCommentsDto from(Notice entity) {
        return new NoticeWithCommentsDto(
                entity.getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getNoticeComments().stream()
                        .map(NoticeCommentDto::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                entity.getTitle(),
                entity.getContent(),
                entity.getHashtag(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

}
