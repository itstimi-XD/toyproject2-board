package fastcampus.toyproject2board.noticeBoard.dto.response;

import fastcampus.toyproject2board.noticeBoard.dto.NoticeDto;

import java.time.LocalDateTime;

public record NoticeResponse(
        Long id,
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        String email,
        String nickname
) {

    public static NoticeResponse of(Long id, String title, String content, String hashtag, LocalDateTime createdAt, String email, String nickname) {
        return new NoticeResponse(id, title, content, hashtag, createdAt, email, nickname);
    }

    public static NoticeResponse from(NoticeDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }

        return new NoticeResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.hashtag(),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname
        );
    }

}
