package fastcampus.toyproject2board.noticeBoard.dto.response;

import fastcampus.toyproject2board.noticeBoard.dto.NoticeCommentDto;

import java.time.LocalDateTime;

public record NoticeCommentResponse(
        Long id,
        String content,
        LocalDateTime createdAt,
        String email,
        String nickname,
        String userId
) {

    public static NoticeCommentResponse of(Long id, String content, LocalDateTime createdAt, String email, String nickname, String userId) {
        return new NoticeCommentResponse(id, content, createdAt, email, nickname, userId);
    }

    public static NoticeCommentResponse from(NoticeCommentDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }

        return new NoticeCommentResponse(
                dto.id(),
                dto.content(),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname,
                dto.userAccountDto().userId()
        );
    }

}
