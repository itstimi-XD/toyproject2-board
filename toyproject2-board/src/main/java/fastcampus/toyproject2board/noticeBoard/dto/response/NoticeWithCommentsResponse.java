package fastcampus.toyproject2board.noticeBoard.dto.response;

import fastcampus.toyproject2board.noticeBoard.dto.NoticeWithCommentsDto;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record NoticeWithCommentsResponse(
        Long id,
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        String email,
        String nickname,
        String userId,
        Set<NoticeCommentResponse> noticeCommentsResponse
) {

    public static NoticeWithCommentsResponse of(Long id, String title, String content, String hashtag, LocalDateTime createdAt, String email, String nickname, String userId, Set<NoticeCommentResponse> noticeCommentResponses) {
        return new NoticeWithCommentsResponse(id, title, content, hashtag, createdAt, email, nickname, userId, noticeCommentResponses);
    }

    public static NoticeWithCommentsResponse from(NoticeWithCommentsDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }

        return new NoticeWithCommentsResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.hashtag(),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname,
                dto.userAccountDto().userId(),
                dto.noticeCommentDtos().stream()
                        .map(NoticeCommentResponse::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }

}
